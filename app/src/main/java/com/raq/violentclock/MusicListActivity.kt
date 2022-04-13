package com.raq.violentclock

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.raq.violentclock.data.SpotifyData
import com.raq.violentclock.service.MusicService


class MusicListActivity : AppCompatActivity() {
    private var listOfSongs = ArrayList<SpotifyData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.musics_list)

    }

    override fun onStart() {
        super.onStart()
        MusicService(this, listOfSongs)
    }

    override fun onStop() {
        super.onStop()
    }
}