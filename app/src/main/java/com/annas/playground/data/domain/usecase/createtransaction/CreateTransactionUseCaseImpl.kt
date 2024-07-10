package com.annas.playground.data.domain.usecase.createtransaction

import com.annas.playground.data.repository.transaction.TransactionRepository

class CreateTransactionUseCaseImpl(
    private val repository: TransactionRepository
) : CreateTransactionUseCase {
    override suspend fun invoke(productId: Int, type: String, amount: Int, date: Long) {
        repository.createTransaction(productId, type, amount, date)
    }
}
