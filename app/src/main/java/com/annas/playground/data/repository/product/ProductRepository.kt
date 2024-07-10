package com.annas.playground.data.repository.product

import com.annas.playground.data.domain.model.Product

interface ProductRepository {
    suspend fun prepareProductData()
    suspend fun getProductList(): List<Product>
    suspend fun getProductById(id: Int): Product
}
