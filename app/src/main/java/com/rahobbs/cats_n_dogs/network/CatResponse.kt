package com.rahobbs.cats_n_dogs.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatResponse(
    @SerialName("file")
    val url: String,
)
