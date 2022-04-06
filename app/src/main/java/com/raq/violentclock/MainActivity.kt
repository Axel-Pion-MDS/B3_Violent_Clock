package com.raq.violentclock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.raq.violentclock.`interface`.SpotifyInterface
import com.raq.violentclock.data.Items
import com.raq.violentclock.data.SpotifyData
import com.raq.violentclock.data.Tracks
import com.raq.violentclock.service.SpotifyService

import com.spotify.android.appremote.api.SpotifyAppRemote
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private var listOfTracks = ArrayList<Items>()
    private var listOfSongs = ArrayList<SpotifyData>()

    private var spotifyAppRemote: SpotifyAppRemote? = null

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
            val spotifyInterface: SpotifyInterface = retrofit.create(SpotifyInterface::class.java)
            val call = spotifyInterface.findSongByName(search = "Kamado")

            call.enqueue(object: Callback<Tracks> {
                override fun onResponse(call: Call<Tracks>, response: Response<Tracks>) {
                    Log.d("MainActivityDebug", "In onResponse()")
                    response.body()?.tracks?.let { responseListOfTracks ->
                        Log.d("MainActivityDebug", responseListOfTracks.toString())

//                        listOfTracks?.clear()
//                        for (track in responseListOfTracks) {
//                            listOfTracks.add(track)
//                        }

//                        listOfSongs?.clear()
//                        for (song in listOfTracks) {
//                            listOfSongs.add(song)
//                        }
                    }
                }
                override fun onFailure(call: Call<Tracks>, throwable: Throwable) {
                    Log.e("MainActivityDebug", throwable.message.toString())
                }
            })
        } catch (e: Exception) {
            Log.e("MainActivityError", e.message.toString())
        }
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
}
