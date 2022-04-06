package com.raq.violentclock

// For navigate
//import android.content.Intent
//val intent = Intent(this, PlayerActivity::class.java)

import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.raq.violentclock.adaptater.AlarmAdaptater
import com.raq.violentclock.data.AlarmData
import com.raq.violentclock.data.Days
import com.raq.violentclock.service.AlarmService
import com.raq.violentclock.service.SpotifyService
import com.spotify.android.appremote.api.SpotifyAppRemote
import java.util.*


class MainActivity : AppCompatActivity() {

    private var spotifyAppRemote: SpotifyAppRemote? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SpotifyService.connect(this) {
            startActivity(intent)
        }
        val onDays = listOf(Days.MONDAY)
        var alarm0 : AlarmData = AlarmData(name = "Réveil 0", hour = Date(), days =  onDays, musique = "test")
        var alarm1 : AlarmData = alarm0.copy(name = "Réveil 1")
        var alarm2 : AlarmData = alarm1.copy(name = "Réveil 2")
        val listOfAlarms = listOf(alarm0, alarm1, alarm2)
        val listOfName = listOf("test")

        val myListAdapter = AlarmAdaptater(this, listOfAlarms, listOfName)
        val listView : ListView = findViewById<ListView>(R.id.alarmsList)
        listView.adapter = myListAdapter

        listView.setOnItemClickListener() { adapterView, view, position, id ->
            val itemAtPos = adapterView.getItemAtPosition(position)
            val itemIdAtPos = adapterView.getItemIdAtPosition(position)
            Toast.makeText(this, "Click on item at $itemAtPos its item id $itemIdAtPos", Toast.LENGTH_LONG).show()
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