package com.raq.violentclock.data

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

data class AlarmData (
    var name: String? = null,
    var hour: Date,
    var days: List<Days>,
    var reccurence: List<Days>? = null,
    var musique: String,
    var isActivated: Boolean = true,
    var onRepeat: Boolean = false
)