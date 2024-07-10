package com.annas.playground.ui.screen.dataprocessing.productdetail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annas.playground.data.domain.model.ProductDetail
import com.annas.playground.data.domain.usecase.getproductdetail.GetProductDetail
import com.annas.playground.ui.graph.RouteParam
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductDetail: GetProductDetail,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productId: Int = savedStateHandle[RouteParam.PRODUCT_ID] ?: 0
    var product = mutableStateOf<ProductDetail?>(null)
        private set

    init {
        getProductDetail()
    }

    fun getProductDetail() = viewModelScope.launch {
        product.value = getProductDetail.invoke(productId)
    }
}
