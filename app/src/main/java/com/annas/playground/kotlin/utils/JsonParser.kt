package com.annas.playground.kotlin.utils

import com.squareup.moshi.Moshi

object JsonParser {
    fun <T> getMockList(filename: String, obj: Class<T>): T? {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(obj)
        val products = this.javaClass.classLoader?.getResource(filename)?.readText()
        val result = products?.run { adapter.fromJson(this) }
        return result
    }
}
