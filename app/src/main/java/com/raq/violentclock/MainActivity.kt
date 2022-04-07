package com.raq.violentclock

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
import java.util.*


class MainActivity : AppCompatActivity() {
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
        val onDays = listOf(Days.MONDAY)
        var alarm0 : AlarmData = AlarmData(name = "Réveil 0", hour = Date(), days =  onDays, musique = "test")
        var alarm1 : AlarmData = alarm0.copy(name = "Réveil 1")
        var alarm2 : AlarmData = alarm1.copy(name = "Réveil 2")
        val listOfAlarms = listOf<AlarmData>(alarm0, alarm1, alarm2)
        AlarmService(this, listOfAlarms)
        registerGlobalEvent()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }
    }

    fun registerGlobalEvent () {
        var btnAlarm : FloatingActionButton = findViewById<FloatingActionButton>(R.id.addAlarm)
        btnAlarm.setOnClickListener {
            Log.d("MainActivityDebug", "Click!")
//            val intent : Intent = Intent(this, AddAlarmActivity::class.java)
            startActivity(intent)
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