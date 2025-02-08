package com.annas.playground.kotlin.data.domain.model

data class EmptyDataException(
    override val message: String
) : Exception()
