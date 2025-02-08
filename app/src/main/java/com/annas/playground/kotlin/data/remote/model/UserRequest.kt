package com.annas.playground.kotlin.data.remote.model

import com.squareup.moshi.Json

data class UserRequest(
    @Json(name = "email")
    val email: String,
    @Json(name = "gender")
    val gender: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "status")
    val status: String
)
