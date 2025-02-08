package com.annas.playground.kotlin.data.local.dto

import com.squareup.moshi.Json

data class MockTransactionDto(
    val type: String,
    @field:Json(name = "product_id") val productId: Int,
    val amount: Int,
    val timestamp: Long,
)
