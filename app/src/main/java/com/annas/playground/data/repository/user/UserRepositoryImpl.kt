package com.annas.playground.data.repository.user

import com.annas.playground.data.domain.model.PagingResponse
import com.annas.playground.data.domain.model.User
import com.annas.playground.data.remote.builder.ApiService
import com.annas.playground.data.remote.model.UserRequest
import com.annas.playground.data.remote.model.asUserDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepositoryImpl(
    private val service: ApiService,
    private val dispatcher: CoroutineDispatcher
) : UserRepository {
    override suspend fun getUser(page: Int): Result<PagingResponse<User>> {
        return kotlin.runCatching {
            delay(2000)
            val response = service.getUser(page)
            val pageSize = response.headers()["x-pagination-pages"]?.toIntOrNull() ?: 1
            val result = PagingResponse(pageSize = pageSize, items = response.body().asUserDomain())
            Result.success(result)
        }.getOrElse { e -> Result.failure(e) }
    }

    override fun postUser(
        email: String,
        name: String,
        gender: String,
        status: String
    ): Flow<Result<Boolean>> = flow {
        val request = UserRequest(email, gender, name, status)
        val response = service.postUser(request)
        emit(Result.success(response.id != null))
    }.catch { e -> emit(Result.failure(e)) }.flowOn(dispatcher)
}
