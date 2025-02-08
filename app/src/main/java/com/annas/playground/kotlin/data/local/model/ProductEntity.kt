package com.annas.playground.kotlin.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.annas.playground.kotlin.data.domain.model.Product

@Entity(tableName = "product")
data class ProductEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
)

fun ProductEntity.asProductDomain(): Product {
    return Product(id = id, name = name, imageUrl = imageUrl)
}

fun List<ProductEntity>.asProductListDomain(): List<Product> {
    return this.map { it.asProductDomain() }
}
