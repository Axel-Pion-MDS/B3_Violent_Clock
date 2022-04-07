package com.raq.violentclock

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.raq.violentclock.`interface`.SpotifyInterface
import com.raq.violentclock.data.*
import com.raq.violentclock.service.AlarmService
import com.raq.violentclock.service.SpotifyService
import com.spotify.android.appremote.api.SpotifyAppRemote
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.*


class MainActivity : AppCompatActivity() {
    private var listOfTracks = ArrayList<Items>()
    private var listOfSongs = ArrayList<SpotifyData>()
    private var userAlarms : ArrayList<AlarmData> = ArrayList()

    private var spotifyAppRemote: SpotifyAppRemote? = null
    private lateinit var spotifyInterface: SpotifyInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
        val alarm : ArrayList<AlarmData>? = intent.getParcelableArrayListExtra("newAlarm")
        if (!alarm.isNullOrEmpty()) {
            Log.d("TEST", alarm.toString())
            userAlarms.addAll(alarm)
        }
//        val gson = Gson()
//        val json = intent.getStringExtra("newAlarm")
//
//        if (!json.isNullOrEmpty()) {
//            val type: Type = object : TypeToken<AlarmData?>() {}.type
//            userAlarms.add(gson.fromJson<AlarmData>(json, type))
//            saveData()
//        }
        registerGlobalEvent()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        //userAlarms.add(AlarmData("Reveil 1", LocalTime.of(12, 10), days = listOf(Days.FRIDAY), musique = "salut"))
        //userAlarms.add(AlarmData("Reveil 2", LocalTime.of(10, 2), days = listOf(Days.FRIDAY), musique = "salut"))
        AlarmService(this, userAlarms)
    }

    override fun onStop() {
        super.onStop()
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }
    }

    private fun registerGlobalEvent () {
        var btnAlarm : FloatingActionButton = findViewById<FloatingActionButton>(R.id.addAlarm)
        btnAlarm.setOnClickListener {
            val intent : Intent = Intent(this, AddAlarmActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("sharedPrefs", AppCompatActivity.MODE_PRIVATE)

        val gson = Gson()
        val json = sharedPreferences.getString("alarms", null)

        val type: Type = object : TypeToken<ArrayList<AlarmData?>?>() {}.type
        userAlarms = gson.fromJson<Any>(json, type) as ArrayList<AlarmData>
        if (userAlarms[0] == null) {
            userAlarms = ArrayList()
        }
    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("sharedPrefs", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val json = gson.toJson(userAlarms)

        editor.putString("alarms", json)

        editor.apply()
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
                            Log.d("SAMEREDEVICEDebug", device_id)

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