package com.raq.violentclock

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.raq.violentclock.*
import com.raq.violentclock.`interface`.SpotifyInterface
import com.raq.violentclock.data.*
import com.raq.violentclock.service.AlarmService
import com.raq.violentclock.utils.SpotifyConstants
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {
    var userBearer: String = "Bearer "
    var weakActivity: WeakReference<MainActivity>? = null

    private var listOfTracks = ArrayList<Items>()
    private var listOfSongs = ArrayList<SpotifyData>()
    private var userAlarms : ArrayList<AlarmData> = ArrayList()

    private var spotifyAppRemote: SpotifyAppRemote? = null
    private lateinit var spotifyInterface: SpotifyInterface

    private lateinit var dataStore: DataStore<Preferences>
    private val activityContext: Activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weakActivity = WeakReference(this@MainActivity)
        setContentView(R.layout.activity_main)

        dataStore = createDataStore(name = "alarms")

//        SpotifyService.connect(this) {}

        val request = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN)
        AuthorizationClient.openLoginActivity(
            this,
            SpotifyConstants.AUTH_TOKEN_REQUEST_CODE,
            request
        )

        val alarm : List<AlarmData>? = intent.getParcelableArrayListExtra("newAlarm")
        if (!alarm.isNullOrEmpty()) {
            var readStorage = lifecycleScope.launch {
                val storageData = read("alarms")
                if (storageData != null) {
                    userAlarms = storageData.toCollection(ArrayList())
                }
                userAlarms.add(alarm[0])
                AlarmService(activityContext, userAlarms)
            }
        }
        registerGlobalEvent()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            val storageData = read("alarms")
            if (storageData != null) {
                userAlarms = storageData.toCollection(ArrayList())
            }
           AlarmService(activityContext, userAlarms)
        }
    }

    override fun onStop() {
        super.onStop()
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }
        lifecycleScope.launch {
            save("alarms", userAlarms)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (SpotifyConstants.AUTH_TOKEN_REQUEST_CODE == requestCode) {
            val response = AuthorizationClient.getResponse(resultCode, data)
            val accessToken: String? = response.accessToken
            userBearer += accessToken.toString()
        }
    }

    private fun getAuthenticationRequest(type: AuthorizationResponse.Type): AuthorizationRequest {
        return AuthorizationRequest.Builder(SpotifyConstants.CLIENT_ID, type, SpotifyConstants.REDIRECT_URI)
            .setShowDialog(false)
            .setScopes(arrayOf("user-modify-playback-state", "user-read-playback-state"))
            .build()
    }

    private fun registerGlobalEvent () {
        var btnAlarm : FloatingActionButton = findViewById<FloatingActionButton>(R.id.addAlarm)
        btnAlarm.setOnClickListener {
            val intent : Intent = Intent(this, AddAlarmActivity::class.java)
            intent.putExtra("userBearer", userBearer)
            startActivity(intent)
        }
    }

    private suspend fun read (key: String): Array<AlarmData>? {
        val dataStoreKey = preferencesKey<String>(key)
        var preferences = dataStore.data.first()
        var save = Gson().fromJson(preferences[dataStoreKey], Array<AlarmData>::class.java)
        return save
    }

    private suspend fun save (key: String, value: ArrayList<AlarmData>) {
        val arrayToJson = Gson().toJson(value)
        val dataStoreKey = preferencesKey<String>(key)
        dataStore.edit { alarms ->
            alarms[dataStoreKey] = arrayToJson
        }
    }

    fun playSong(bearer : String, song: String) {
        var device_id: String = ""

        try {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.spotify.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            spotifyInterface = retrofit.create(SpotifyInterface::class.java)
            var call = spotifyInterface.getDevices(header = bearer)
            val spotifyDevice = SpotifyDevice()


            call.enqueue(object: Callback<Devices> {
                override fun onResponse(call: Call<Devices>, response: Response<Devices>) {
                    response.body()?.devices?.let { responseListOfDevices ->
                        Log.d("SpotifyDebug", responseListOfDevices.toString())

                        device_id = spotifyDevice.normalize(responseListOfDevices)
                        Log.d("SpotifyDebug", device_id)

                        try {
                            Log.d("SpotifyDebug", device_id)

                            val callSong = spotifyInterface.playSong(header = bearer, device_id = device_id, body = SpotifyPostSong(context_uri = song))
                            callSong.enqueue(object: Callback<String> {
                                override fun onResponse(callSong: Call<String>, response: Response<String>) {
                                    response.body()?.let { responseSong ->
                                        Log.d("SpotifyDebug", responseSong)
                                    }
                                }
                                override fun onFailure(call: Call<String>, throwable: Throwable) {
                                    Log.e("SpotifyDebug", throwable.message.toString())
                                }
                            })
                        } catch (e: Exception) {
                            Log.e("SpotifyDebug", e.message.toString())
                        }

                    }
                }
                override fun onFailure(call: Call<Devices>, throwable: Throwable) {
                    Log.e("SpotifyDebug", throwable.message.toString())
                }
            })

        } catch (e: Exception) {
            Log.e("SpotifyDebug", e.message.toString())
        }
    }

    fun stopSong(bearer : String) {
        var device_id: String = ""

        try {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.spotify.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            spotifyInterface = retrofit.create(SpotifyInterface::class.java)
            var call = spotifyInterface.getDevices(header = bearer)
            val spotifyDevice = SpotifyDevice()

            call.enqueue(object: Callback<Devices> {
                override fun onResponse(call: Call<Devices>, response: Response<Devices>) {
                    response.body()?.devices?.let { responseListOfDevices ->
                        Log.d("SpotifyPlayDebug", responseListOfDevices.toString())

                        device_id = spotifyDevice.normalize(responseListOfDevices)
                        Log.d("SpotifyPlayDebug", device_id)

                        try {
                            Log.d("SpotifyPlayDebug", device_id)

                            val callSong = spotifyInterface.stopSong(header = bearer, device_id = device_id)
                            callSong.enqueue(object: Callback<String> {
                                override fun onResponse(callSong: Call<String>, response: Response<String>) {
                                    Log.d("SpotifyPlayDebug", response.toString())
                                    response.body()?.let { responseSong ->
                                        Log.d("SpotifyPlayDebug", responseSong)
                                    }
                                }
                                override fun onFailure(call: Call<String>, throwable: Throwable) {
                                    Log.e("SpotifyPlayError", throwable.message.toString())
                                }
                            })
                        } catch (e: Exception) {
                            Log.e("SpotifyPlayError", e.message.toString())
                        }

                    }
                }
                override fun onFailure(call: Call<Devices>, throwable: Throwable) {
                    Log.e("SpotifyPlayError", throwable.message.toString())
                }
            })

        } catch (e: Exception) {
            Log.e("SpotifyPlayError", e.message.toString())
        }
    }

}