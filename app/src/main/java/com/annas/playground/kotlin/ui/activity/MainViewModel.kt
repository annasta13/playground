package com.annas.playground.kotlin.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annas.playground.kotlin.data.domain.usecase.prepareasset.PrepareMockDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val prepareMockDataUseCase: PrepareMockDataUseCase
) : ViewModel() {

    fun prepareProduct() {
        viewModelScope.launch {
            prepareMockDataUseCase.invoke()
        }
    }
}
