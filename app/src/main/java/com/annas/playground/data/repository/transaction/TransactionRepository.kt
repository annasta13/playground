package com.annas.playground.data.repository.transaction

import com.annas.playground.data.domain.model.Transaction

interface TransactionRepository {
    suspend fun prepareTransactionData()
    suspend fun getTransactionByProductId(productId: Int): List<Transaction>
    suspend fun getTransactionInByProductId(productId: Int): Int
    suspend fun getTransactionOutByProductId(productId: Int): Int
    suspend fun createTransaction(productId: Int, type: String, amount: Int, date: Long)
}
