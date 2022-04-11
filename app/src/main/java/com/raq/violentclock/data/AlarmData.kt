package com.raq.violentclock.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalTime
import java.util.*

enum class Days(name: String) {
    MONDAY("Lundi"),
    TUESDAY("Mardi"),
    WEDNESDAY("Mercredi"),
    THURSDAY("Jeudi"),
    FRIDAY("Vendredi"),
    SATURDAY("Samedi"),
    SUNDAY("Dimanche")
}

@Parcelize
data class AlarmData (
    var name: String = "Alarme",
    var hour: String,
    var days: List<Days>,
    var reccurence: Boolean = false,
    var musique: String,
    var isActivated: Boolean = true,
    var onRepeat: Boolean = false
) : Parcelable