package com.annas.playground.kotlin.data.domain.usecase.getproductdetail

import com.annas.playground.kotlin.data.domain.model.ProductDetail

interface GetProductDetail {

    suspend operator fun invoke(productId: Int): ProductDetail

}
