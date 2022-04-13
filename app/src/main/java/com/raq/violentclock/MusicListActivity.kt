package com.raq.violentclock

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.raq.violentclock.`interface`.SpotifyInterface
import com.raq.violentclock.data.SpotifyData
import com.raq.violentclock.data.Tracks
import com.raq.violentclock.service.MusicService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MusicListActivity : AppCompatActivity() {
    private var listOfSongs = ArrayList<SpotifyData>()
    private lateinit var spotifyInterface: SpotifyInterface

    var userSong : String = "spotify:artist:7i3eGEz3HNFnPOCdc7mqoq"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.musics_list)
        registerGlobalEvent()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    private fun registerGlobalEvent () {
        var searchBtn : ImageButton = findViewById<ImageButton>(R.id.searchButton)
        var saveBtn : FloatingActionButton = findViewById<FloatingActionButton>(R.id.saveMusic)
        searchBtn.setOnClickListener {
            var searchText : String = findViewById<EditText>(R.id.searchMusic).toString()
            listOfSongs = searchSongs(searchText)
            MusicService(this, listOfSongs)
        }
        saveBtn.setOnClickListener {
            val intent : Intent = Intent(this, AddAlarmActivity::class.java)
            intent.putExtra("addSongName", userSong)
            startActivity(intent)
        }
    }

    private fun searchSongs (searchValue : String) : ArrayList<SpotifyData> {
        var listOfSongs : ArrayList<SpotifyData> = ArrayList()
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.spotify.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            spotifyInterface = retrofit.create(SpotifyInterface::class.java)
            val call = spotifyInterface.findSongByName(search = searchValue)
            call.enqueue(object: Callback<Tracks> {
                override fun onResponse(call: Call<Tracks>, response: Response<Tracks>) {
                    response.body()?.tracks?.let { responseListOfTracks ->
                        Log.d("MusicListActivity", responseListOfTracks.toString())
                        // TODO @Axel
                        // Push all array of SpotifyData type in listOfSongs
                        // If all works well, check front and you have a list of 20 songs
                        // I hope
                    }
                }
                override fun onFailure(call: Call<Tracks>, throwable: Throwable) {
                    Log.e("MusicListActivity", throwable.message.toString())
                }
            })

        } catch (e: Exception) {
            Log.e("MusicListActivity", e.message.toString())
        }
        return listOfSongs
    }
}