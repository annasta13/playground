package com.annas.playground.kotlin.data.domain.usecase.getuser

import com.annas.playground.kotlin.data.domain.model.PagingResponse
import com.annas.playground.kotlin.data.domain.model.User

interface GetUserUseCase {

    suspend operator fun invoke(page: Int): Result<PagingResponse<User>>
}
