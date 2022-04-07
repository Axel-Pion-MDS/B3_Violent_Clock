package com.raq.violentclock

import android.util.Log
import com.raq.violentclock.data.Devices
import com.raq.violentclock.data.SpotifyDevices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SpotifyDevice {
    var device_id: String = ""

    fun normalize(responseListOfDevices: ArrayList<SpotifyDevices>): String {
        for (device in responseListOfDevices) {
            if (device.type == "Smartphone") device_id = device.id
        }
        Log.d("SpotifyDeviceDebug", device_id)

        return device_id
    }
}