package com.example.gaurichugh

import com.squareup.moshi.Json

data class Photo(
    val id: Int,
    val width: Int,
    val height: Int,
    @Json(name = "src") val source: Source
)

data class Source(
    @Json(name = "original") val original: String,
    @Json(name = "large2x") val large2x: String
)
