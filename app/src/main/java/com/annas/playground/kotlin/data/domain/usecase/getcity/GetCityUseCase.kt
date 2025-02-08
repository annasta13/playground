package com.annas.playground.kotlin.data.domain.usecase.getcity

interface GetCityUseCase {
    suspend operator fun invoke(): List<String>
}

