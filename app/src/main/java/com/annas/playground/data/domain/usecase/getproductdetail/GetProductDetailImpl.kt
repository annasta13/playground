package com.annas.playground.data.domain.usecase.getproductdetail

import com.annas.playground.data.domain.model.ProductDetail
import com.annas.playground.data.repository.product.ProductRepository
import com.annas.playground.data.repository.transaction.TransactionRepository

class GetProductDetailImpl(
    private val productRepository: ProductRepository,
    private val transactionRepository: TransactionRepository
) : GetProductDetail {
    override suspend fun invoke(productId: Int): ProductDetail {
        val product = productRepository.getProductById(productId)
        val transactions = transactionRepository.getTransactionByProductId(productId)
        val totalProductIn = transactionRepository.getTransactionInByProductId(productId)
        val totalProductOut = transactionRepository.getTransactionOutByProductId(productId)
        return ProductDetail(
            id = productId,
            name = product.name,
            imageUrl = product.imageUrl,
            transactionList = transactions,
            stock = totalProductIn - totalProductOut
        )
    }
}
