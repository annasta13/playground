package com.annas.playground.ui.screen.dataprocessing.createtransaction

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annas.playground.constants.StringConstant
import com.annas.playground.constants.StringConstant.AMOUNT_EXCEEDS_STOCK_WARNING
import com.annas.playground.constants.StringConstant.AMOUNT_INVALID_WARNING
import com.annas.playground.data.domain.model.Product
import com.annas.playground.data.domain.usecase.createtransaction.CreateTransactionUseCase
import com.annas.playground.data.domain.usecase.getproductoverview.GetProductOverviewUseCase
import com.annas.playground.data.local.model.TransactionEntity
import com.annas.playground.ui.graph.RouteParam
import com.annas.playground.utils.LongExtension.toDateString
import com.annas.playground.utils.StringExtension.toMilliseconds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTransactionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val getProductOverviewUseCase: GetProductOverviewUseCase
) : ViewModel() {

    private val productId: Int = savedStateHandle[RouteParam.PRODUCT_ID] ?: 0
    val minTransactionDate: Long = savedStateHandle[RouteParam.MIN_DATE] ?: 0L
    val stock: Int = savedStateHandle[RouteParam.STOCK] ?: 0
    var timeInput = mutableStateOf(System.currentTimeMillis().toDateString())
        private set
    var amountInput = mutableStateOf("")
        private set
    var transactionTypeInput = mutableStateOf(Pair(StringConstant.SELLING, TransactionEntity.OUT))
        private set

    var product = mutableStateOf<Product?>(null)
        private set

    init {
        viewModelScope.launch {
            product.value = getProductOverviewUseCase.invoke(productId)
        }
    }

    fun submit(onSuccess: () -> Unit, onError: (String) -> Unit) = viewModelScope.launch {
        kotlin.runCatching {
            val amount = amountInput.value.toInt()
            when {
                amount < 1 -> onError(AMOUNT_INVALID_WARNING)
                !amountIsSufficient(amount) -> onError(AMOUNT_EXCEEDS_STOCK_WARNING)
                else -> saveTransaction(onSuccess)
            }
        }.onFailure { it.message?.let(onError) }
    }

    private suspend fun saveTransaction(onSuccess: () -> Unit) {
        createTransactionUseCase.invoke(
            type = transactionTypeInput.value.second,
            date = timeInput.value.toMilliseconds(),
            productId = productId,
            amount = amountInput.value.toInt()
        )
        onSuccess.invoke()
    }

    private fun amountIsSufficient(amount: Int): Boolean {
        return transactionTypeInput.value.second == TransactionEntity.OUT && amount <= stock
    }
}

