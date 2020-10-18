package com.rahobbs.cats_n_dogs.network

import junit.framework.Assert.assertEquals
import kotlinx.serialization.json.Json
import org.junit.Test

class DogResponseTest {
    companion object {
        const val sampleResp =
            """{"fileSizeBytes":71671,"url":"https://random.dog/0b0b349e-47e2-4e1e-987d-975f01d2c4f9.jpg"}"""
        const val sampleImageUri = "https://random.dog/0b0b349e-47e2-4e1e-987d-975f01d2c4f9.jpg"
    }

    @Test
    fun decodeResponse() {
        val formatter = Json {
            encodeDefaults = false
            ignoreUnknownKeys = true
            isLenient = true
        }

        val resp = formatter.decodeFromString(DogResponse.serializer(), sampleResp)
        assertEquals(
            sampleImageUri,
            resp.url
        )
    }
}
