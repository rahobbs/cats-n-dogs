package com.rahobbs.cats_n_dogs

import android.location.Location
import java.math.BigDecimal
import java.math.RoundingMode

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
