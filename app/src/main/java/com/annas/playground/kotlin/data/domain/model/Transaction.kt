package com.annas.playground.kotlin.data.domain.model

data class Transaction(
    val id: Int,
    val type: String,
    val date: String,
    val amount: Int,
)
