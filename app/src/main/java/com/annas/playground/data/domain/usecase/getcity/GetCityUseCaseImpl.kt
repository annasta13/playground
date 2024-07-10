package com.annas.playground.data.domain.usecase.getcity

import com.annas.playground.data.repository.city.CityRepository

class GetCityUseCaseImpl(private val repository: CityRepository) : GetCityUseCase {
    override suspend fun invoke(): List<String> {
        return repository.getCity()
    }

}
