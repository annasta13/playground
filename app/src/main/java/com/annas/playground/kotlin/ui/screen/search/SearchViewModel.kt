package com.annas.playground.kotlin.ui.screen.search

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annas.playground.kotlin.data.domain.usecase.getcity.GetCityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val getCityUseCase: GetCityUseCase) :
    ViewModel() {

    private var cityList = emptyList<String>()

    var listShowing = mutableStateListOf<String>()
        private set
    var searchKey = mutableStateOf("")
        private set

    private fun getCities(onFinish: () -> Unit) {
        viewModelScope.launch {
            cityList = getCityUseCase.invoke()
            onFinish()
        }
    }

    fun searchCity() {
        if (cityList.isEmpty()) getCities(::filterKey)
        else filterKey()
    }

    private fun filterKey() = viewModelScope.launch {
        val listToShow = cityList.filter { it.contains(searchKey.value.trim(), true) }
        listShowing.clear()
        listShowing.addAll(listToShow)
    }
}
