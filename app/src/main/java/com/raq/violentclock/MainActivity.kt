package com.raq.violentclock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.raq.violentclock.service.SpotifyService

import com.spotify.android.appremote.api.SpotifyAppRemote


class MainActivity : AppCompatActivity() {

    private val clientId = "14471ab9b43741e8b6eefea6c0947938"
    private val redirectUri = "http://com.raq.violentclock/callback"
    private var spotifyAppRemote: SpotifyAppRemote? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SpotifyService.connect(this) {
            val intent = Intent(this, this::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
//        spotifyAppRemote?.let {
//            SpotifyAppRemote.disconnect(it)
//        }
    }
}