package com.raq.violentclock

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.raq.violentclock.data.AlarmData
import com.raq.violentclock.service.AlarmService
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.Temporal
import java.util.*
import kotlin.collections.ArrayList

class AddAlarmActivity : AppCompatActivity() {

    private var mainActivity : Activity = Activity()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_alarm)
        var picker : TimePicker = findViewById<TimePicker>(R.id.timePicker)
        var date = LocalDateTime.now();
        var formatter = DateTimeFormatter.ofPattern("HH")
        picker.hour = date.format(formatter).toInt()
        formatter = DateTimeFormatter.ofPattern("mm")
        picker.minute = date.format(formatter).toInt()
        picker.setIs24HourView(true)

        registerGlobalEvent()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun registerGlobalEvent () {
        var goback : Button = findViewById<Button>(R.id.goBack)
        var addAlarm : Button = findViewById<Button>(R.id.addAlarmFromNewAlarm)
        goback.setOnClickListener {
            val intent : Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        addAlarm.setOnClickListener {
            val newAlarm : AlarmData = addAlarm()
            val intent = Intent(this, MainActivity::class.java)
            intent.putParcelableArrayListExtra("newAlarm", arrayListOf(newAlarm))
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addAlarm() : AlarmData {
        var alarmName : EditText = findViewById<EditText>(R.id.alarmNameInput)
        var alarmMusic : EditText = findViewById<EditText>(R.id.alarmMusic)
        var alarmTime : TimePicker = findViewById<TimePicker>(R.id.timePicker)
        var isRepeating : CheckBox = findViewById<CheckBox>(R.id.isRepeating)


        var time : LocalTime = LocalTime.of(alarmTime.hour, alarmTime.minute)

        var newAlarm = AlarmData(hour = time, days = listOf(), reccurence = isRepeating.isChecked, musique = alarmMusic.text.toString())
        if (alarmName.text.toString() != "") {
            newAlarm.name = alarmName.text.toString()
        }

        return newAlarm
    }
}