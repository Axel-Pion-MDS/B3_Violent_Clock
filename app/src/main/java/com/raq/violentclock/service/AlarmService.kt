package com.raq.violentclock.service

import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import com.raq.violentclock.R
import com.raq.violentclock.data.AlarmData

class AlarmService (userAlarms : List<AlarmData>){
    var userAlarms = userAlarms

    init {
        Log.d("AlarmServiceDebug", "AlarmService started!")
    }
}