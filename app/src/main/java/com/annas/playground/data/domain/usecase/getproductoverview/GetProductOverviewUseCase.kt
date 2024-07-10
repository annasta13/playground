package com.annas.playground.data.domain.usecase.getproductoverview

import com.annas.playground.data.domain.model.Product

interface GetProductOverviewUseCase {

    suspend operator fun invoke(productId: Int): Product

}
