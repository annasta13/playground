package com.annas.playground.kotlin.data.domain.usecase.postuser

import com.annas.playground.kotlin.data.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow

class PostUserUseCaseImpl(private val repository: UserRepository) : PostUserUseCase {
    override fun invoke(
        email: String,
        name: String,
        gender: String,
        status: String
    ): Flow<Result<Boolean>> {
        return repository.postUser(email, name, gender, status)
    }
}
