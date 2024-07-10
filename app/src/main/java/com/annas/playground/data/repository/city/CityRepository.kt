package com.annas.playground.data.repository.city

interface CityRepository {
    suspend fun getCity(): List<String>
}
