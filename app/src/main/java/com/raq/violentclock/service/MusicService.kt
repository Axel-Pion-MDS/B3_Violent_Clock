package com.raq.violentclock.service

import android.app.Activity
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.raq.violentclock.R
import com.raq.violentclock.adaptater.AlarmAdaptater
import com.raq.violentclock.adaptater.MusicAdaptater
import com.raq.violentclock.data.AlarmData
import com.raq.violentclock.data.SpotifyData

class MusicService (activity : Activity, musicList : List<SpotifyData>){
    private var activity = activity

    init {
        val listOfMusisName = mutableListOf<String>()

        if (!musicList.isNullOrEmpty()) {
            val nothingYet : TextView = activity.findViewById<TextView>(R.id.nothingYet)
            nothingYet.visibility = View.INVISIBLE

            musicList.forEach { alarm ->
                listOfMusisName.add(alarm.name)
            }
            val myListAdapter = MusicAdaptater(activity, musicList, listOfMusisName)
            val listView : ListView = activity.findViewById<ListView>(R.id.alarmsList)
            listView.adapter = myListAdapter

            listView.isClickable = true
            listView.setOnItemClickListener() { adapterView, view, position, id ->
                val itemAtPos = adapterView.getItemAtPosition(position)
                val itemIdAtPos = adapterView.getItemIdAtPosition(position)
            }
        }
    }
}