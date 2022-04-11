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
import java.time.LocalTime

class AlarmAdaptater (private val context: Activity, private val alarms: List<AlarmData>, private val listOfAlarmsName: List<String>)
    : ArrayAdapter<String>(context, R.layout.listview_item_alarm, listOfAlarmsName) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.listview_item_alarm, null, true)

        val alarmHour : TextView = rowView.findViewById(R.id.musicName)
        val alarmName : TextView = rowView.findViewById(R.id.musicArtist)

        val time : List<String> = alarms[position].hour.split(":")
        val hour: Int = time[0].toInt()
        val min: Int = time[1].toInt()

        alarmHour.text = "${if (hour < 10) "0${hour}" else hour}:${if (min < 10) "0${min}" else min}"
        alarmName.text = alarms[position].name

        return rowView
    }
}