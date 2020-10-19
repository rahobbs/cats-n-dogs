package com.rahobbs.cats_n_dogs.network

import com.soywiz.klock.Time
import junit.framework.Assert.assertEquals
import kotlinx.serialization.json.Json
import org.junit.Test

class SunriseSunsetTest {
    companion object {
        const val sampleResp =
            """{"results":{"sunrise":"12:43:55 AM","sunset":"1:22:23 PM","solar_noon":"7:03:09 AM","day_length":"12:38:28","civil_twilight_begin":"12:16:07 AM","civil_twilight_end":"1:50:10 PM","nautical_twilight_begin":"11:43:10 PM","nautical_twilight_end":"2:23:08 PM","astronomical_twilight_begin":"11:09:10 PM","astronomical_twilight_end":"2:57:08 PM"},"status":"OK"}"""
        val sampleSunrise = Time(12, 43, 55, 0)
        val sampleSunset = Time(13, 22, 23, 0)
    }

    @Test
    fun decodeResponse() {
        val formatter = Json {
            encodeDefaults = false
            ignoreUnknownKeys = true
            isLenient = true
        }
        val resp = formatter.decodeFromString(SunRiseSetResponse.serializer(), sampleResp)
        println("Resp: $resp")
        assertEquals(
            sampleSunrise,
            resp.results.sunrise.toTime()
        )
        assertEquals(sampleSunset, resp.results.sunset.toTime())
    }
}
