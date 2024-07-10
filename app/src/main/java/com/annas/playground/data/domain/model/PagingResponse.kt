package com.annas.playground.data.domain.model

data class PagingResponse<T>(
    val pageSize: Int,
    val items: List<T>
)
