package com.annas.playground.kotlin.di

import com.annas.playground.kotlin.data.domain.usecase.createtransaction.CreateTransactionUseCase
import com.annas.playground.kotlin.data.domain.usecase.createtransaction.CreateTransactionUseCaseImpl
import com.annas.playground.kotlin.data.local.db.ProductDatabase
import com.annas.playground.kotlin.data.repository.transaction.TransactionRepository
import com.annas.playground.kotlin.data.repository.transaction.TransactionRepositoryImpl
import com.annas.playground.kotlin.helper.common.DataHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TransactionModule {

    @Singleton
    @Provides
    fun provideTransactionRepository(
        database: ProductDatabase,
        dataHelper: DataHelper
    ): TransactionRepository {
        return TransactionRepositoryImpl(database.transactionDao(), dataHelper)
    }

    @Singleton
    @Provides
    fun provideCreateTransactionUseCase(
        transactionRepository: TransactionRepository
    ): CreateTransactionUseCase {
        return CreateTransactionUseCaseImpl(transactionRepository)
    }

}
