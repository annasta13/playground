package com.annas.playground.data.domain.usecase.getproductdetail

import com.annas.playground.data.domain.model.ProductDetail

interface GetProductDetail {

    suspend operator fun invoke(productId: Int): ProductDetail

}
