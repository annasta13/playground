package com.annas.playground.kotlin.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.annas.playground.kotlin.data.local.model.TransactionEntity

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(list: List<TransactionEntity>)

    @Query("SELECT COUNT(id) FROM `transaction`")
    suspend fun countTransaction(): Int

    @Query("SELECT * FROM `transaction` WHERE product_id=:productId")
    suspend fun getTransactionsByProductId(productId: Int): List<TransactionEntity>

    @Query("SELECT SUM(amount) FROM `transaction` WHERE product_id=:productId AND type='${TransactionEntity.IN}'")
    suspend fun getTransactionInByProductId(productId: Int): Int

    @Query("SELECT SUM(amount) FROM `transaction` WHERE product_id=:productId AND type='${TransactionEntity.OUT}'")
    suspend fun getTransactionOutByProductId(productId: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createTransaction(transaction: TransactionEntity)
}
