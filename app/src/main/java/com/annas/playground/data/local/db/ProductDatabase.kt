package com.annas.playground.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.annas.playground.data.local.dao.ProductDao
import com.annas.playground.data.local.dao.TransactionDao
import com.annas.playground.data.local.model.ProductEntity
import com.annas.playground.data.local.model.ProductStockView
import com.annas.playground.data.local.model.TransactionEntity

@Database(
    entities = [
        ProductEntity::class,
        TransactionEntity::class
    ],
    version = 1,
    exportSchema = true,
    views = [ProductStockView::class]
)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        const val DATABASE_NAME: String = "product-database"
    }
}
