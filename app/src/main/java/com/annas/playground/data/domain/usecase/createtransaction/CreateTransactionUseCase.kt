package com.annas.playground.data.domain.usecase.createtransaction

interface CreateTransactionUseCase {
    suspend operator fun invoke(
        productId: Int,
        type: String,
        amount: Int,
        date: Long
    )
}
