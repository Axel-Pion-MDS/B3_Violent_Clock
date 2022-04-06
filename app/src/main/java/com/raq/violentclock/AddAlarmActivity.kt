package com.raq.violentclock

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddAlarmActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_alarm)
        var picker : TimePicker = findViewById<TimePicker>(R.id.timePicker)
        var date = LocalDateTime.now();
        var formatter = DateTimeFormatter.ofPattern("HH")
        picker.hour = date.format(formatter).toInt()
        formatter = DateTimeFormatter.ofPattern("mm")
        picker.hour = date.format(formatter).toInt()
        picker.setIs24HourView(true)
    }
}