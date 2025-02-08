/*
 * Copyright (c) Habil Education 2023. All rights reserved.
 */

package com.annas.playground.kotlin.data.remote.builder

import com.annas.playground.kotlin.data.remote.model.UserRequest
import com.annas.playground.kotlin.data.remote.model.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("public/v2/users")
    suspend fun getUser(@Query("page") page: Int): Response<List<UserResponse>>

    @POST("public/v2/users")
    suspend fun postUser(@Body request: UserRequest): UserResponse

}
