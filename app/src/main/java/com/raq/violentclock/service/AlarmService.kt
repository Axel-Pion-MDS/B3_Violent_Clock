package com.raq.violentclock.service

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.raq.violentclock.R
import com.raq.violentclock.adaptater.AlarmAdaptater
import com.raq.violentclock.data.AlarmData
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

class AlarmService (activity : Activity, userAlarms : ArrayList<AlarmData>?){
    private var activity = activity
    private var userAlarms : ArrayList<AlarmData>? = userAlarms

    init {
        Log.d("TEST", "AlarmService started! $userAlarms")

        val listOfAlarmsName = mutableListOf<String>()

        if (!userAlarms.isNullOrEmpty()) {
            Log.d("TEST", "SALUT")
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