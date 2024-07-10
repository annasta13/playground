package com.annas.playground.data.domain.model

data class ProductDetail(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val stock: Int,
    val transactionList: List<Transaction>
)
