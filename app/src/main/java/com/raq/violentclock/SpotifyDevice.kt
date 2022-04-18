package com.raq.violentclock

import android.util.Log
import com.raq.violentclock.data.SpotifyDevices

class SpotifyDevice {
    var device_id: String = ""

    fun normalize(responseListOfDevices: ArrayList<SpotifyDevices>): String {
        for (device in responseListOfDevices) {
            if (device.type == "Smartphone") device_id = device.id
        }
        Log.d("SpotifyDebug", device_id)

        return device_id
    }
}