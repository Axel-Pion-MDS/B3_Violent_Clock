package com.raq.violentclock.service

import android.app.Activity
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import com.raq.violentclock.R
import com.raq.violentclock.adaptater.AlarmAdaptater
import com.raq.violentclock.data.AlarmData
import java.util.*

class AlarmService (activity : Activity, userAlarms : List<AlarmData>){
    private var userAlarms = userAlarms
    private var activity = activity

    init {
        Log.d("AlarmServiceDebug", "AlarmService started!")

        val listOfAlarmsName = mutableListOf<String>()
        userAlarms.forEach { alarm ->
            listOfAlarmsName.add(alarm.name)
        }

        val myListAdapter = AlarmAdaptater(activity, userAlarms, listOfAlarmsName)
        val listView : ListView = activity.findViewById<ListView>(R.id.alarmsList)
        listView.adapter = myListAdapter

        listView.isClickable = true
        listView.setOnItemClickListener() { adapterView, view, position, id ->
            val itemAtPos = adapterView.getItemAtPosition(position)
            val itemIdAtPos = adapterView.getItemIdAtPosition(position)
            Toast.makeText(activity, "Click on item at $itemAtPos its item id $itemIdAtPos", Toast.LENGTH_LONG).show()
            Log.d("AlarmService", "Click on item at $itemAtPos its item id $itemIdAtPos")
        }
    }
}