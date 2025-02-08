package com.annas.playground.kotlin.ui.screen.dataprocessing.productlist

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annas.playground.kotlin.data.domain.model.Product
import com.annas.playground.kotlin.data.domain.usecase.getproductlist.GetProductList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getProductList: GetProductList
) : ViewModel() {

    var list = mutableStateListOf<Product>()
        private set

    init {
        getProductList()
    }

    private fun getProductList() {
        viewModelScope.launch {
            if (list.isNotEmpty()) list.clear()
            list.addAll(getProductList.invoke())
        }
    }
}
