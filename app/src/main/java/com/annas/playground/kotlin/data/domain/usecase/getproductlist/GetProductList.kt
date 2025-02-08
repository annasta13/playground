package com.annas.playground.kotlin.data.domain.usecase.getproductlist

import com.annas.playground.kotlin.data.domain.model.Product

interface GetProductList {
    suspend operator fun invoke(): List<Product>
}
