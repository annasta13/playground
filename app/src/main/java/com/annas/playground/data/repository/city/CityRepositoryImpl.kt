package com.annas.playground.data.repository.city

import com.squareup.moshi.Moshi

class CityRepositoryImpl : CityRepository {
    override suspend fun getCity(): List<String> {
        val resource = this.javaClass.classLoader?.getResource("city-resource.json")?.readText()
        val result = resource?.run {
            val moshi = Moshi.Builder().build()
            val adapter = moshi.adapter(Array<String>::class.java)
            adapter.fromJson(this)
        }
        return result?.toList().orEmpty()
    }
}
