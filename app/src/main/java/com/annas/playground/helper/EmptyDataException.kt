package com.annas.playground.helper

data class EmptyDataException(
    override val message: String
) : Exception()
