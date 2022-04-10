package com.raq.violentclock.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi

class AlarmBroadcastReceiver : BroadcastReceiver() {
    @RequiresApi(api = Build.VERSION_CODES.Q)
    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "Alarm!", Toast.LENGTH_LONG).show()
        var alarmUri: Uri? = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }
        // setting default ringtone
        val ringtone = RingtoneManager.getRingtone(context, alarmUri)
        // play ringtone
        ringtone.play()
    }
}