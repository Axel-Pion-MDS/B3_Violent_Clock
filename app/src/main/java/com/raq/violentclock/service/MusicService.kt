package com.raq.violentclock.service

import android.app.Activity
import android.content.Intent.getIntent
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.core.view.get
import com.raq.violentclock.R
import com.raq.violentclock.adaptater.MusicAdaptater
import com.raq.violentclock.data.SpotifyData

class MusicService (activity : Activity, musicList : List<SpotifyData>){
    private var activity = activity

    init {
        Log.d("MusicListDebug", musicList.toString())
        val listOfMusisName = mutableListOf<String>()

        if (!musicList.isNullOrEmpty()) {
            val nothingYet : TextView = activity.findViewById<TextView>(R.id.nothingYet)
            nothingYet.visibility = View.INVISIBLE

            musicList.forEach { music ->
                listOfMusisName.add(music.name)
            }
            val myListAdapter = MusicAdaptater(activity, musicList, listOfMusisName)
            val listView : ListView = activity.findViewById<ListView>(R.id.musicList)
            listView.adapter = myListAdapter

            listView.isClickable = true

            listView.setOnItemClickListener { adapterView, view, position, id ->
                Log.d("MusicServiceDebug", adapterView.toString())
                Log.d("MusicServiceDebug", view.toString())
                Log.d("MusicServiceDebug", position.toString())
                Log.d("MusicServiceDebug", id.toString())
                // TODO @Axel
                // If user click on a song play it
                // On click talk with MusicListActivity and edit userSong var string with code you want for playing the song
                // I don't know how sorry :'(
            }
        }
    }
}