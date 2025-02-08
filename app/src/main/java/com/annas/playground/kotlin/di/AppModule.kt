package com.annas.playground.kotlin.di

import android.content.Context
import androidx.room.Room
import com.annas.playground.kotlin.App
import com.annas.playground.kotlin.data.domain.usecase.getcity.GetCityUseCase
import com.annas.playground.kotlin.data.domain.usecase.getcity.GetCityUseCaseImpl
import com.annas.playground.kotlin.data.domain.usecase.prepareasset.PrepareInitialDataUseCaseImpl
import com.annas.playground.kotlin.data.domain.usecase.prepareasset.PrepareMockDataUseCase
import com.annas.playground.kotlin.data.local.db.ProductDatabase
import com.annas.playground.kotlin.data.repository.city.CityRepository
import com.annas.playground.kotlin.data.repository.city.CityRepositoryImpl
import com.annas.playground.kotlin.data.repository.product.ProductRepository
import com.annas.playground.kotlin.data.repository.transaction.TransactionRepository
import com.annas.playground.kotlin.helper.common.DataHelper
import com.annas.playground.kotlin.helper.common.PreferenceHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): App {
        return app as App
    }
    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext app: Context): PreferenceHelper {
        return PreferenceHelper(app)
    }
    @Singleton
    @Provides
    fun provideProductDatabase(@ApplicationContext context: Context): ProductDatabase {
        return Room.databaseBuilder(
            context,
            ProductDatabase::class.java,
            ProductDatabase.DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Singleton
    @Provides
    fun provideCityRepository(): CityRepository {
        return CityRepositoryImpl()
    }

    @Singleton
    @Provides
    fun provideGetCityUseCase(repository: CityRepository): GetCityUseCase {
        return GetCityUseCaseImpl(repository)
    }


    @Singleton
    @Provides
    fun provideDataHelper() = DataHelper()

    @Singleton
    @Provides
    fun providePrepareMockDataUseCase(
        productRepository: ProductRepository,
        transactionRepository: TransactionRepository
    ): PrepareMockDataUseCase {
        return PrepareInitialDataUseCaseImpl(productRepository, transactionRepository)
    }

}
