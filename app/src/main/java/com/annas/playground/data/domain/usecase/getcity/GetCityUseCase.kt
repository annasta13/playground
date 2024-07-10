package com.annas.playground.data.domain.usecase.getcity

interface GetCityUseCase {
    suspend operator fun invoke(): List<String>
}

