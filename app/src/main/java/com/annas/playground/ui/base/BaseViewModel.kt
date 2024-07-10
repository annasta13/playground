package com.annas.playground.ui.base

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    fun execute(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch(block = block)

    fun showLoading() {
        isLoading.value = true
        setError(null)
    }

    fun dismissLoading() {
        isLoading.value = false
    }

    fun setError(message: String?) {
        errorMessage.value = message
        isLoading.value = errorMessage.value != null
    }
}
