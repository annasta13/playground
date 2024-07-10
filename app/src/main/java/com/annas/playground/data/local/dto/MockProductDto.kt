package com.annas.playground.data.local.dto

import com.squareup.moshi.Json

data class MockProductDto(
    val id: Int,
    val name: String,
    @field:Json(name = "image_url") val imageUrl: String
)
