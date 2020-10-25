package com.rahobbs.cats_n_dogs.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data as returned by the Sunset and Sunrise times api.
 *
 * Created by BillH on 3/3/2019
 */
@Serializable
data class SunRiseSetResponse(
    val results: SunRiseSetResults,
    val status: String
)

/**
 * The results object returned by the end point.
 */
@Serializable
data class SunRiseSetResults(
    @SerialName("sunrise")
    val sunrise: ApiTime,
    @SerialName("sunset")
    val sunset: ApiTime,
    @SerialName("solar_noon")
    val solarNoon: ApiTime? = null,
    @SerialName("civil_twilight_begin")
    val civilTwilightBegin: ApiTime? = null,
    @SerialName("civil_twilight_end")
    val civilTwilightEnd: ApiTime? = null,
    @SerialName("nautical_twilight_begin")
    val nauticalTwilightBegin: ApiTime? = null,
    @SerialName("nautical_twilight_end")
    val NauticalTwilightEnd: ApiTime? = null,
    @SerialName("astronomical_twilight_begin")
    val astronomicalTwilightBegin: ApiTime? = null,
    @SerialName("astronomical_twilight_end")
    val astronomicalTwilightEnd: ApiTime? = null
)