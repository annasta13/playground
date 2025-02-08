package com.annas.playground.kotlin.data.domain.usecase.getproductoverview

import com.annas.playground.kotlin.data.domain.model.Product

interface GetProductOverviewUseCase {

    suspend operator fun invoke(productId: Int): Product

}
