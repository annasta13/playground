package com.annas.playground.kotlin.data.domain.usecase.getproductlist

import com.annas.playground.kotlin.data.domain.model.Product
import com.annas.playground.kotlin.data.repository.product.ProductRepository

class GetProductListImpl(private val repository: ProductRepository) : GetProductList {
    override suspend fun invoke(): List<Product> {
        return repository.getProductList()
    }
}
