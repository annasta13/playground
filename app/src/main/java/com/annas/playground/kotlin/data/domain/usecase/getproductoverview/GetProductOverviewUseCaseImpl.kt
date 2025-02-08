package com.annas.playground.kotlin.data.domain.usecase.getproductoverview

import com.annas.playground.kotlin.data.domain.model.Product
import com.annas.playground.kotlin.data.repository.product.ProductRepository

class GetProductOverviewUseCaseImpl(
    private val repository: ProductRepository
) : GetProductOverviewUseCase {
    override suspend fun invoke(productId: Int): Product {
        return repository.getProductById(productId)
    }
}
