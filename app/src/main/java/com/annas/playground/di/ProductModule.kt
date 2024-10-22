package com.annas.playground.di

import com.annas.playground.data.domain.usecase.getproductdetail.GetProductDetail
import com.annas.playground.data.domain.usecase.getproductdetail.GetProductDetailImpl
import com.annas.playground.data.domain.usecase.getproductlist.GetProductList
import com.annas.playground.data.domain.usecase.getproductlist.GetProductListImpl
import com.annas.playground.data.domain.usecase.getproductoverview.GetProductOverviewUseCase
import com.annas.playground.data.domain.usecase.getproductoverview.GetProductOverviewUseCaseImpl
import com.annas.playground.data.local.db.ProductDatabase
import com.annas.playground.data.repository.product.ProductRepository
import com.annas.playground.data.repository.product.ProductRepositoryImpl
import com.annas.playground.data.repository.transaction.TransactionRepository
import com.annas.playground.helper.DataHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductModule {

    @Singleton
    @Provides
    fun provideProductRepository(
        productDatabase: ProductDatabase,
        dataHelper: DataHelper
    ): ProductRepository {
        return ProductRepositoryImpl(productDatabase.productDao(), dataHelper)
    }

    @Singleton
    @Provides
    fun provideGetProductListUseCase(repository: ProductRepository): GetProductList {
        return GetProductListImpl(repository)
    }

    @Singleton
    @Provides
    fun provideGetProductDetailUseCase(
        repository: ProductRepository,
        transactionRepository: TransactionRepository
    ): GetProductDetail {
        return GetProductDetailImpl(repository, transactionRepository)
    }

    @Singleton
    @Provides
    fun provideGetProductOverviewUseCase(
        repository: ProductRepository
    ): GetProductOverviewUseCase {
        return GetProductOverviewUseCaseImpl(repository)
    }


}
