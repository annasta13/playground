package com.annas.playground.data.repository.product

import com.annas.playground.data.domain.model.Product
import com.annas.playground.data.local.dao.ProductDao
import com.annas.playground.data.local.model.asProductDomain
import com.annas.playground.data.local.model.asProductListDomain
import com.annas.playground.helper.DataHelper

class ProductRepositoryImpl(
    private val productDao: ProductDao,
    private val dataHelper: DataHelper
) : ProductRepository {
    override suspend fun getProductList(): List<Product> {
        return productDao.getProductList().asProductListDomain()
    }

    override suspend fun getProductById(id: Int): Product {
        return productDao.getProductById(id).asProductDomain()
    }

    override suspend fun prepareProductData() {
        val productCount = productDao.countProduct()
        if (productCount == 0) {
            val products = dataHelper.getMockProducts()
            productDao.insertProducts(products)
        }
    }
}
