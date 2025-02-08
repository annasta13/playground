package com.annas.playground.kotlin.data.remote.builder

import com.annas.playground.kotlin.constants.LongConstant
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitBuilder {
    fun create(): ApiService {
        val client = OkHttpClient.Builder()
            .connectTimeout(LongConstant.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(LongConstant.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(HeaderInterceptor())
            .build()
        return Retrofit.Builder()
            .client(client)
            .baseUrl("https://gorest.co.in/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
