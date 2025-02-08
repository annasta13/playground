package com.annas.playground.kotlin.data.domain.usecase.getcity

import com.annas.playground.kotlin.data.repository.city.CityRepository

class GetCityUseCaseImpl(private val repository: CityRepository) : GetCityUseCase {
    override suspend fun invoke(): List<String> {
        return repository.getCity()
    }

}
