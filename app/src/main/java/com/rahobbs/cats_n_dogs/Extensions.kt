package com.rahobbs.cats_n_dogs

import android.location.Location
import com.rahobbs.cats_n_dogs.network.SunRiseSetResults
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ofPattern
import java.util.*

fun Location.formattedLatitude(): String {
    return BigDecimal(this.latitude).setScale(
        2,
        RoundingMode.HALF_EVEN
    ).toString()
}

fun Location.formattedLongitude(): String {
    return BigDecimal(this.longitude).setScale(
        2,
        RoundingMode.HALF_EVEN
    ).toString()
}


fun SunRiseSetResults.isDaytime(): Boolean {
    val now = LocalTime.now()
    val localOffset: Long =
        ZoneId.systemDefault().rules.getOffset(LocalDateTime.now()).totalSeconds / 3600L

    // Since ApiTime values are in UTC, and since they lack Date information to easily convert to
    // DateTime, we convert to a LocalTime and add the system default timezone offset
    val sunriseDateTime = LocalTime.parse(this.sunrise.toTime().toString()).plusHours(localOffset)
    val sunsetDateTime = LocalTime.parse(this.sunset.toTime().toString()).plusHours(localOffset)

    // If it's after sunrise and before sunset, we consider it daytime
    return now.isAfter(sunriseDateTime) && now.isBefore(sunsetDateTime)
}

fun SunRiseSetResults.nextSolarEventTimeString(): String {
    val localOffset: Long =
        ZoneId.systemDefault().rules.getOffset(LocalDateTime.now()).totalSeconds / 3600L
    val dateTimeFormatter = ofPattern("hh:mm a")

    return if (this.isDaytime()) {
        LocalTime.parse(this.sunset.toTime().toString()).plusHours(localOffset)
            .format(dateTimeFormatter)
    } else {
        LocalTime.parse(this.sunrise.toTime().toString()).plusHours(localOffset)
            .format(dateTimeFormatter)
    }
}
