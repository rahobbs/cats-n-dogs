package com.rahobbs.cats_n_dogs.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Deferred
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.sunrise-sunset.org"

private val sunRiseSetContentType = MediaType.parse("application/json")

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json {
        // Ignore unknown keys to ensure new properties on the response don't crash the app
        ignoreUnknownKeys = true
    }.asConverterFactory(sunRiseSetContentType!!))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface SunRiseSetApiService {
    @GET("/json")
    fun getSunRiseSetAsync(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double
    ): Deferred<SunRiseSetResponse>
}

object SunRiseSetApi {
    val retrofitService: SunRiseSetApiService by lazy {
        retrofit.create(SunRiseSetApiService::class.java)
    }
}
