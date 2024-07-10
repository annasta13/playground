package com.annas.playground.data.domain.usecase.getproductlist

import com.annas.playground.data.domain.model.Product

interface GetProductList {
    suspend operator fun invoke(): List<Product>
}
