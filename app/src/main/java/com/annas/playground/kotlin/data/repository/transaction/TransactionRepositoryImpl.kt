package com.annas.playground.kotlin.data.repository.transaction

import com.annas.playground.kotlin.data.domain.model.Transaction
import com.annas.playground.kotlin.data.local.dao.TransactionDao
import com.annas.playground.kotlin.data.local.model.TransactionEntity
import com.annas.playground.kotlin.data.local.model.asTransactionDomain
import com.annas.playground.kotlin.helper.common.DataHelper

class TransactionRepositoryImpl(
    private val dao: TransactionDao,
    private val dataHelper: DataHelper
) : TransactionRepository {

    override suspend fun prepareTransactionData() {
        if (dao.countTransaction() == 0) {
            val transactionsIn = dataHelper.getMockTransactions(TransactionEntity.IN)
            val transactionsOut = dataHelper.getMockTransactions(TransactionEntity.OUT)
            dao.insertTransactions(transactionsIn)
            dao.insertTransactions(transactionsOut)
        }
    }

    override suspend fun getTransactionByProductId(productId: Int): List<Transaction> {
        return dao.getTransactionsByProductId(productId).asTransactionDomain()
    }

    override suspend fun getTransactionInByProductId(productId: Int): Int {
        return dao.getTransactionInByProductId(productId)
    }

    override suspend fun getTransactionOutByProductId(productId: Int): Int {
        return dao.getTransactionOutByProductId(productId)
    }

    override suspend fun createTransaction(productId: Int, type: String, amount: Int, date: Long) {
        val transaction = TransactionEntity(
            id = 0,
            productId = productId,
            type = type,
            amount = amount,
            timestamp = date
        )
        dao.createTransaction(transaction)
    }
}
