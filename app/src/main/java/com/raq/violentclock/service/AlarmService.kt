package com.raq.violentclock.service

import android.app.Activity
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.raq.violentclock.R
import com.raq.violentclock.adaptater.AlarmAdaptater
import com.raq.violentclock.data.AlarmData
import kotlin.collections.ArrayList

class AlarmService (activity : Activity, userAlarms : ArrayList<AlarmData>?){
    private var activity = activity
    private var userAlarms : ArrayList<AlarmData>? = userAlarms

    init {
        val listOfAlarmsName = mutableListOf<String>()

        if (!userAlarms.isNullOrEmpty()) {
            val nothingYet : TextView = activity.findViewById<TextView>(R.id.nothingYet)
            nothingYet.visibility = View.INVISIBLE

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
            }
        }
    }
}