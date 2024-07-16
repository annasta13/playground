package com.annas.playground.data.domain.usecase.prepareasset

import com.annas.playground.data.repository.product.ProductRepository
import com.annas.playground.data.repository.transaction.TransactionRepository

class PrepareInitialDataUseCaseImpl(
    private val productRepository: ProductRepository,
    private val transactionRepository: TransactionRepository
) : PrepareMockDataUseCase {
    override suspend operator fun invoke() {
        productRepository.prepareProductData()
        transactionRepository.prepareTransactionData()
    }
}
