package com.annas.playground.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.annas.playground.data.domain.model.Product
import com.annas.playground.data.local.model.ProductEntity
import com.annas.playground.data.local.model.ProductStockView
import com.annas.playground.data.local.model.TransactionEntity

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(list: List<ProductEntity>)

    @Query("SELECT COUNT(id) FROM product")
    suspend fun countProduct(): Int

    @Query("SELECT * FROM product")
    suspend fun getProductList(): List<ProductEntity>
    @Query("SELECT * FROM product WHERE id=:id")
    suspend fun getProductById(id: Int): ProductEntity
}
