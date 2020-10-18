package com.rahobbs.cats_n_dogs.network

import kotlinx.serialization.Serializable

@Serializable
data class DogResponse(
    val fileSizeBytes: Long,
    val url: String,
)
