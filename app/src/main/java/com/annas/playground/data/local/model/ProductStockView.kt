package com.annas.playground.data.local.model

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Entity

@DatabaseView(
    "SELECT p.id as product_id, name, image_url, amount, timestamp " +
            "FROM product as p INNER JOIN `transaction` as t ON p.id=t.product_id WHERE type='IN'"
)
@Entity
data class ProductStockView(
    @ColumnInfo(name = "product_id") val productId: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image_url") val image: String,
    @ColumnInfo(name = "amount") val amount: Int,
    @ColumnInfo(name = "timestamp") val month: Int
)
