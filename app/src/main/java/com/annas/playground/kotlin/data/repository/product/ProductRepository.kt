package com.annas.playground.kotlin.data.repository.product

import com.annas.playground.kotlin.data.domain.model.Product

interface ProductRepository {
    suspend fun prepareProductData()
    suspend fun getProductList(): List<Product>
    suspend fun getProductById(id: Int): Product
}
