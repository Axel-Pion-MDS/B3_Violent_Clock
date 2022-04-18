package com.raq.violentclock

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.raq.violentclock.data.AlarmData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddAlarmActivity : AppCompatActivity() {

    private var mainActivity : Activity = Activity()
    private var userBearer : String = "Bearer"

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

        val music : String? = intent.getStringExtra("addSongName")
        if (!music.isNullOrEmpty()) {
            var songInput : TextView = findViewById<TextView>(R.id.userMusic)
            songInput.text = music
        }
        userBearer = intent.getStringExtra("userBearer").toString()
        registerGlobalEvent()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun registerGlobalEvent () {
        var goback : Button = findViewById<Button>(R.id.goBack)
        var addAlarm : Button = findViewById<Button>(R.id.addAlarmFromNewAlarm)
        var addSong : Button = findViewById<Button>(R.id.alarmMusicBtn)
        goback.setOnClickListener {
            val intent : Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        addAlarm.setOnClickListener {
            val newAlarm : AlarmData = addAlarm()
            val intent = Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putParcelableArrayListExtra("newAlarm", arrayListOf(newAlarm))
            startActivity(intent)
        }
        addSong.setOnClickListener {
            val intent : Intent = Intent(this, MusicListActivity::class.java)
            intent.putExtra("userBearer", userBearer)
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addAlarm() : AlarmData {
        var alarmName : EditText = findViewById<EditText>(R.id.alarmNameInput)
        var alarmMusic : TextView = findViewById<TextView>(R.id.userMusic)
        var alarmTime : TimePicker = findViewById<TimePicker>(R.id.timePicker)
        var isRepeating : CheckBox = findViewById<CheckBox>(R.id.isRepeating)


        var time : String = "${alarmTime.hour}:${alarmTime.minute}"

        var newAlarm = AlarmData(hour = time, days = listOf(), reccurence = isRepeating.isChecked, musique = alarmMusic.text.toString())
        if (alarmName.text.toString() != "") {
            newAlarm.name = alarmName.text.toString()
        }

        return newAlarm
    }
}