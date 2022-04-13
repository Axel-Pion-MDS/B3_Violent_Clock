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
import androidx.datastore.preferences.preferencesKey
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.raq.violentclock.`interface`.SpotifyInterface
import com.raq.violentclock.data.*
import com.raq.violentclock.service.SpotifyService
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList
import com.google.gson.Gson
import com.raq.violentclock.service.AlarmService


class MainActivity : AppCompatActivity() {
    private var listOfTracks = ArrayList<Items>()
    private var listOfSongs = ArrayList<SpotifyData>()
    private var userAlarms : ArrayList<AlarmData> = ArrayList()

    private var spotifyAppRemote: SpotifyAppRemote? = null
    private lateinit var spotifyInterface: SpotifyInterface

    private lateinit var dataStore: DataStore<Preferences>
    private val activityContext: Activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataStore = createDataStore(name = "alarms")

        SpotifyService.connect(this) {
//            val intent = Intent(this, this::class.java)
//            startActivity(intent)
        }
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.spotify.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            spotifyInterface = retrofit.create(SpotifyInterface::class.java)
            val call = spotifyInterface.findSongByName(search = "Kamado")

            call.enqueue(object: Callback<Tracks> {
                override fun onResponse(call: Call<Tracks>, response: Response<Tracks>) {
                    response.body()?.tracks?.let { responseListOfTracks ->
                        val spotifySearch: SpotifySearch = SpotifySearch()
                        val normalize = spotifySearch.normalize(responseListOfTracks)
                        Log.d("MainActivityDebug", normalize)
                    }
                }
                override fun onFailure(call: Call<Tracks>, throwable: Throwable) {
                    Log.e("MainActivityError", throwable.message.toString())
                }
            })
        } catch (e: Exception) {
            Log.e("MainActivityError", e.message.toString())
        }
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

    private fun registerGlobalEvent () {
        var btnAlarm : FloatingActionButton = findViewById<FloatingActionButton>(R.id.addAlarm)
        var btnPlay : FloatingActionButton = findViewById<FloatingActionButton>(R.id.startMusic)
        btnAlarm.setOnClickListener {
            val intent : Intent = Intent(this, AddAlarmActivity::class.java)
            startActivity(intent)
        }
        btnPlay.setOnClickListener {
            playSong()
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

    fun playSong() {
        var device_id: String = ""

        try {
            var call = spotifyInterface.getDevices()
            val spotifyDevice = SpotifyDevice()
            Log.d("SpotifyPlayDebug", spotifyDevice.toString())

            call.enqueue(object: Callback<Devices> {
                override fun onResponse(call: Call<Devices>, response: Response<Devices>) {
                    response.body()?.devices?.let { responseListOfDevices ->
                        Log.d("SpotifyPlayDebug", responseListOfDevices.toString())

                        device_id = spotifyDevice.normalize(responseListOfDevices)
                        Log.d("SpotifyPlayDebug", device_id)

                        try {
                            Log.d("SpotifyPlayDebug", device_id)

                            val callSong = spotifyInterface.playSong(device_id = device_id, body = SpotifyPostSong("spotify:artist:7i3eGEz3HNFnPOCdc7mqoq"))
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