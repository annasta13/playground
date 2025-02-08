/*
 * Copyright (c) Habil Education 2023. All rights reserved.
 */

package com.annas.playground.kotlin.data.domain.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.annas.playground.kotlin.constants.StringConstant

class ListPagingSource<T : Any>(
    private val invoke: suspend (Int) -> Result<PagingResponse<T>>,

    ) : PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val currentPage = params.key ?: 1
        val response = invoke(currentPage)
        return if (response.isSuccess) {
            val pageSize = response.getOrNull()?.pageSize ?: 1
            val data = response.getOrNull()?.items.orEmpty()
            val nextKey = if (pageSize == currentPage) null else currentPage + 1
            val prevKey = if (currentPage > 1 && data.isNotEmpty()) currentPage - 1 else null
            if (currentPage == 1 && data.isEmpty()) {
                LoadResult.Error(EmptyDataException(StringConstant.DATA_NOT_FOUND))
            } else {
                LoadResult.Page(
                    data = data,
                    nextKey = nextKey,
                    prevKey = prevKey
                )
            }
        } else if (response.exceptionOrNull() != null) {
            LoadResult.Error(response.exceptionOrNull()!!)
        } else LoadResult.Error(Exception(StringConstant.SOMETHING_WENT_WRONG))
    }
}
