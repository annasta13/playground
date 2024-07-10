package com.annas.playground.data.domain.usecase.getuser

import com.annas.playground.data.domain.model.PagingResponse
import com.annas.playground.data.domain.model.User
import com.annas.playground.data.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUserUseCaseImpl(private val repository: UserRepository) : GetUserUseCase {
    override suspend fun invoke(page: Int): Result<PagingResponse<User>> {
        return repository.getUser(page)
    }
}
