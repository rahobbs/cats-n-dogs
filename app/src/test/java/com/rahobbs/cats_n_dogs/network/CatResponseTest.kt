package com.rahobbs.cats_n_dogs.network

import junit.framework.Assert.assertEquals
import kotlinx.serialization.json.Json
import org.junit.Test

class CatResponseTest {
    companion object {
        const val sampleResp =
            """{"file":"https:\/\/purr.objects-us-east-1.dream.io\/i\/image5.jpg"}"""
        const val sampleImageUri = "https://purr.objects-us-east-1.dream.io/i/image5.jpg"
    }

    @Test
    fun decodeResponse() {
        val formatter = Json {
            encodeDefaults = false
            ignoreUnknownKeys = true
            isLenient = true
        }

        val resp = formatter.decodeFromString(CatResponse.serializer(), sampleResp)
        assertEquals(
            sampleImageUri,
            resp.file
        )
    }
}
