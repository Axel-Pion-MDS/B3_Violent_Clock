package com.raq.violentclock

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.raq.violentclock.`interface`.SpotifyInterface
import com.raq.violentclock.adaptater.MusicAdaptater
import com.raq.violentclock.data.SpotifyData
import com.raq.violentclock.data.Tracks
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MusicListActivity : AppCompatActivity() {
    private var listOfSongs = ArrayList<SpotifyData>()
    private lateinit var spotifyInterface: SpotifyInterface
    private lateinit var activity: AppCompatActivity
    private var userBearer : String = "Bearer"

    var userSong : String = ""
    var isPlaying : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        setContentView(R.layout.musics_list)
        userBearer = intent.getStringExtra("userBearer").toString()
        registerGlobalEvent()
    }

    private fun registerGlobalEvent () {
        var searchBtn : ImageButton = findViewById<ImageButton>(R.id.searchButton)
        var saveBtn : FloatingActionButton = findViewById<FloatingActionButton>(R.id.saveMusic)
        searchBtn.setOnClickListener {
            var searchText : String =  findViewById<EditText>(R.id.searchMusic).text.toString()
            searchSongs(searchText)
        }
        saveBtn.setOnClickListener {
            MainActivity().stopSong(userBearer)
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

            val call = spotifyInterface.findSongByName(header = userBearer, search = searchValue)
            call.enqueue(object: Callback<Tracks> {
                override fun onResponse(call: Call<Tracks>, response: Response<Tracks>) {
                    Log.d("MusicListActivity", response.toString())

                    response.body()?.tracks?.let { responseListOfTracks ->
                        val listOfMusicsName = mutableListOf<String>()
                        listOfSongs.clear()
                        for (data in responseListOfTracks.items) {
                            listOfSongs.add(data.album)
                            listOfMusicsName.add(data.album.name)
                        }
                        val adapter = MusicAdaptater(activity, listOfSongs, listOfMusicsName)

                        val listView = findViewById<ListView>(R.id.musicList)

                        listView.isClickable = true

                        listView.setOnItemClickListener { _, view, _, _ ->
                            var song: TextView = view.findViewById<TextView>(R.id.musicId)
                            var statusPic: ImageView = view.findViewById<ImageView>(R.id.statusImage)
                            if (userSong != song.text.toString()) isPlaying = false
                            if (isPlaying) {
                                MainActivity().stopSong(userBearer)
                                statusPic.setImageResource(android.R.drawable.ic_media_play)
                            } else {
                                MainActivity().playSong(userBearer, song.text.toString())
                                statusPic.setImageResource(android.R.drawable.ic_media_pause)
                            }
                            isPlaying = !isPlaying
                            userSong = song.text.toString()
                        }
                        listView.adapter = adapter
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