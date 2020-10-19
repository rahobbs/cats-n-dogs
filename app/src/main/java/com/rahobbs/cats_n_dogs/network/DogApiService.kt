package com.rahobbs.cats_n_dogs.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Deferred
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.http.GET

private const val BASE_URL = "https://random.dog"

private val dogContentType = MediaType.parse("application/json")

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory(dogContentType!!))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface DogApiService {
    @GET("/woof.json")
    fun getNewDogAsync(): Deferred<DogResponse>
}

object DogApi {
    val retrofitService: DogApiService by lazy {
        retrofit.create(DogApiService::class.java)
    }
}
