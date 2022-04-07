package com.raq.violentclock.data

data class Devices(
    val devices: ArrayList<SpotifyDevices>
)

data class SpotifyDevices(
    val id: String,
    val type: String
)
