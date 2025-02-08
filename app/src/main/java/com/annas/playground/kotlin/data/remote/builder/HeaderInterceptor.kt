package com.annas.playground.kotlin.data.remote.builder

import com.annas.playground.BuildConfig
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val bearer = "Bearer ${BuildConfig.ACCESS_TOKEN}"
        val builder = Headers.Builder()
        builder.add("Authorization", bearer)
        val request = chain.request().newBuilder()
            .headers(builder.build())
            .build()
        return chain.proceed(request)
    }
}
