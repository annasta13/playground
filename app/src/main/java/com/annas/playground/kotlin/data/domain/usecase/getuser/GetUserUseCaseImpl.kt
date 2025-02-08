package com.annas.playground.kotlin.data.domain.usecase.getuser

import com.annas.playground.kotlin.data.domain.model.PagingResponse
import com.annas.playground.kotlin.data.domain.model.User
import com.annas.playground.kotlin.data.repository.user.UserRepository

class GetUserUseCaseImpl(private val repository: UserRepository) : GetUserUseCase {
    override suspend fun invoke(page: Int): Result<PagingResponse<User>> {
        return repository.getUser(page)
    }
}
