package com.annas.playground.data.domain.usecase.getproductoverview

import com.annas.playground.data.domain.model.Product
import com.annas.playground.data.repository.product.ProductRepository

class GetProductOverviewUseCaseImpl(
    private val repository: ProductRepository
) : GetProductOverviewUseCase {
    override suspend fun invoke(productId: Int): Product {
        return repository.getProductById(productId)
    }
}
