package com.annas.playground.data.domain.usecase.postuser

import kotlinx.coroutines.flow.Flow

interface PostUserUseCase {
    operator fun invoke(
        email: String,
        name: String,
        gender: String,
        status: String
    ): Flow<Result<Boolean>>
}
