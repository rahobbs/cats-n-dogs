package com.rahobbs.cats_n_dogs.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Deferred
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.http.GET

private const val BASE_URL = "https://aws.random.cat"

val contentType = MediaType.parse("application/json")

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory(contentType!!))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface CatApiService {
    @GET("/meow")
    fun getNewCatAsync(): Deferred<CatResponse>
}

object CatApi {
    val retrofitService: CatApiService by lazy {
        retrofit.create(CatApiService::class.java)
    }
}
