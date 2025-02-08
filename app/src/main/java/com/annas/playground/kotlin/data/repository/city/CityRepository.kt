package com.annas.playground.kotlin.data.repository.city

interface CityRepository {
    suspend fun getCity(): List<String>
}
