package com.annas.playground.di

import com.annas.playground.data.domain.usecase.createtransaction.CreateTransactionUseCase
import com.annas.playground.data.domain.usecase.createtransaction.CreateTransactionUseCaseImpl
import com.annas.playground.data.local.db.ProductDatabase
import com.annas.playground.data.repository.transaction.TransactionRepository
import com.annas.playground.data.repository.transaction.TransactionRepositoryImpl
import com.annas.playground.helper.DataHelper
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
