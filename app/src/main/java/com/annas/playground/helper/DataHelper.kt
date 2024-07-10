package com.annas.playground.helper

import com.annas.playground.data.local.dto.MockProductDto
import com.annas.playground.data.local.dto.MockTransactionDto
import com.annas.playground.data.local.model.ProductEntity
import com.annas.playground.data.local.model.TransactionEntity
import com.annas.playground.utils.JsonParser

class DataHelper {

    fun getMockProducts(): List<ProductEntity> {
        val result = JsonParser.getMockList("products.json", Array<MockProductDto>::class.java)
        return result?.map { it.run { ProductEntity(id, name, imageUrl) } }.orEmpty()
    }

    fun getMockTransactions(transactionType: String): List<TransactionEntity> {
        val result =
            JsonParser.getMockList(
                "transactions-$transactionType.json",
                Array<MockTransactionDto>::class.java
            )
        return result?.map {
            it.run { TransactionEntity(0, type, productId, amount, timestamp) }
        }.orEmpty()
    }
}
