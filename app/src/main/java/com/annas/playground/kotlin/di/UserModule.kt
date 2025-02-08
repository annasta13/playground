package com.annas.playground.kotlin.di

import com.annas.playground.kotlin.data.domain.usecase.getuser.GetUserUseCase
import com.annas.playground.kotlin.data.domain.usecase.getuser.GetUserUseCaseImpl
import com.annas.playground.kotlin.data.domain.usecase.postuser.PostUserUseCase
import com.annas.playground.kotlin.data.domain.usecase.postuser.PostUserUseCaseImpl
import com.annas.playground.kotlin.data.remote.builder.ApiService
import com.annas.playground.kotlin.data.remote.builder.RetrofitBuilder
import com.annas.playground.kotlin.data.repository.user.UserRepository
import com.annas.playground.kotlin.data.repository.user.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {
    @Singleton
    @Provides
    fun provideApiService(): ApiService {
        return RetrofitBuilder.create()
    }

    @Singleton
    @Provides
    fun provideUserRepository(
        service: ApiService,
        dispatcher: CoroutineDispatcher
    ): UserRepository {
        return UserRepositoryImpl(service, dispatcher)
    }

    @Singleton
    @Provides
    fun provideGetUserUseCase(repository: UserRepository): GetUserUseCase {
        return GetUserUseCaseImpl(repository)
    }

    @Singleton
    @Provides
    fun providePostUserUseCase(repository: UserRepository): PostUserUseCase {
        return PostUserUseCaseImpl(repository)
    }
}
