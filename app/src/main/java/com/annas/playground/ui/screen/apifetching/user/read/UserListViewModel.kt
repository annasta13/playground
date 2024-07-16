package com.annas.playground.ui.screen.apifetching.user.read

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.annas.playground.data.domain.model.ListPagingSource
import com.annas.playground.data.domain.usecase.getuser.GetUserUseCase
import com.annas.playground.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(private val getUserUseCase: GetUserUseCase) :
    BaseViewModel() {

    var list = Pager(config = PagingConfig(pageSize = 1)) {
        ListPagingSource(invoke = { getUserUseCase.invoke(page = it) })
    }.flow
        private set

    fun fetchUsers() = execute {
        list = flowOf(PagingData.empty())
        list = Pager(PagingConfig(pageSize = 1)) {
            ListPagingSource(invoke = { getUserUseCase.invoke(page = it) })
        }.flow.cachedIn(viewModelScope)
    }
}
