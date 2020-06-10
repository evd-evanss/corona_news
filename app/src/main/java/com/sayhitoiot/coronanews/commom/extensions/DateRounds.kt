package com.sayhitoiot.coronanews.commom.extensions

import java.util.*

fun Date.roundToHourDown() : Date {
    val calendar = Calendar.getInstance()
    calendar.time = this

    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return calendar.time
}