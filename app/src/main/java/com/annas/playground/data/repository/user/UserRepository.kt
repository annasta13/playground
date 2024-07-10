package com.annas.playground.data.repository.user

import com.annas.playground.data.domain.model.PagingResponse
import com.annas.playground.data.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUser(page: Int): Result<PagingResponse<User>>
    fun postUser(
        email: String,
        name: String,
        gender: String,
        status: String
    ): Flow<Result<Boolean>>
}
