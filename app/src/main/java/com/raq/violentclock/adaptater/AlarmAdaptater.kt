package com.raq.violentclock.adaptater

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.raq.violentclock.R
import com.raq.violentclock.data.AlarmData
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

class AlarmAdaptater (private val context: Activity, private val alarms: List<AlarmData>, private val listOfAlarmsName: List<String>)
    : ArrayAdapter<String>(context, R.layout.listview_item, listOfAlarmsName) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.listview_item, null, true)

        val alarmHour : TextView = rowView.findViewById(R.id.alarmHour)
        val alarmName : TextView = rowView.findViewById(R.id.alarmName)

        var hourFormat = SimpleDateFormat("HH:mm")

        alarmHour.text = hourFormat.format(alarms[position].hour)
        alarmName.text = alarms[position].name

        return rowView
    }
}