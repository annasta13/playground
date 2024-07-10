package com.annas.playground.data.remote.model

import com.annas.playground.data.domain.model.User
import com.squareup.moshi.Json

data class UserResponse(
    @field:Json(name = "email")
    val email: String?,
    @field:Json(name = "gender")
    val gender: String?,
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "name")
    val name: String?,
    @field:Json(name = "status")
    val status: String?
)

fun List<UserResponse>?.asUserDomain(): List<User> {
    return this?.map {
        it.run {
            User(
                email = email.orEmpty(),
                gender = gender.orEmpty(),
                id = id ?: 0,
                name = name.orEmpty(),
                status = status.orEmpty()
            )
        }
    }.orEmpty()
}
