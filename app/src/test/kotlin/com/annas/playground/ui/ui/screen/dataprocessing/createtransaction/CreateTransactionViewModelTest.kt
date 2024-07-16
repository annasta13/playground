package com.annas.playground.ui.ui.screen.dataprocessing.createtransaction

import androidx.lifecycle.SavedStateHandle
import com.annas.playground.constants.StringConstant
import com.annas.playground.data.domain.usecase.createtransaction.CreateTransactionUseCase
import com.annas.playground.data.domain.usecase.createtransaction.CreateTransactionUseCaseImpl
import com.annas.playground.data.domain.usecase.getproductoverview.GetProductOverviewUseCase
import com.annas.playground.data.domain.usecase.getproductoverview.GetProductOverviewUseCaseImpl
import com.annas.playground.data.local.dao.ProductDao
import com.annas.playground.data.local.dao.TransactionDao
import com.annas.playground.data.local.model.ProductEntity
import com.annas.playground.data.local.model.TransactionEntity
import com.annas.playground.data.repository.product.ProductRepository
import com.annas.playground.data.repository.product.ProductRepositoryImpl
import com.annas.playground.data.repository.transaction.TransactionRepository
import com.annas.playground.data.repository.transaction.TransactionRepositoryImpl
import com.annas.playground.helper.DataHelper
import com.annas.playground.ui.graph.RouteParam
import com.annas.playground.ui.helper.BaseTest
import com.annas.playground.ui.screen.dataprocessing.createtransaction.CreateTransactionViewModel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.doReturn

class CreateTransactionViewModelTest : BaseTest() {
    private lateinit var transactionDao: TransactionDao
    private lateinit var getProductOverviewUseCase: GetProductOverviewUseCase
    private lateinit var createTransactionUseCase: CreateTransactionUseCase
    private lateinit var productDao: ProductDao
    private lateinit var dataHelper: DataHelper
    private val productId = 1
    private lateinit var product: ProductEntity
    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setup() {
        dataHelper = DataHelper()
        transactionDao = Mockito.mock()
        productDao = Mockito.mock()
        product = dataHelper.getMockProducts().first { it.id == productId }
        val transactionRepository: TransactionRepository =
            TransactionRepositoryImpl(transactionDao, dataHelper)
        val productRepository: ProductRepository = ProductRepositoryImpl(productDao, dataHelper)
        runBlocking { prepareDataTest() }
        savedStateHandle = SavedStateHandle().apply {
            set(RouteParam.PRODUCT_ID, productId)
            set(RouteParam.MIN_DATE, System.currentTimeMillis())
            set(RouteParam.STOCK, 300)
        }
        getProductOverviewUseCase =
            GetProductOverviewUseCaseImpl(productRepository)
        createTransactionUseCase = CreateTransactionUseCaseImpl(transactionRepository)
    }

    private suspend fun prepareDataTest() {
        Mockito.`when`(productDao.getProductById(productId)) doReturn product
        Mockito.`when`(transactionDao.insertTransactions(Mockito.anyList())) doReturn Unit
        Mockito.`when`(productDao.countProduct()) doReturn 300
    }

    @Test
    fun whenCreateTransaction_transactionAdded() = runTest {
        //GIVEN
        val viewModel = CreateTransactionViewModel(
            savedStateHandle = savedStateHandle,
            createTransactionUseCase = createTransactionUseCase,
            getProductOverviewUseCase = getProductOverviewUseCase,
        )
        var success = false

        //WHEN
        viewModel.transactionTypeInput.value = Pair(StringConstant.SELLING, TransactionEntity.OUT)
        viewModel.amountInput.value = 299.toString()
        viewModel.submit(onSuccess = { success = true }, onError = {
            println("error $it")
        })

        //THEN
        assert(success)
    }

    @Test
    fun givenInsufficientAmount_whenCreateTransaction_amountInvalidErrorInvoked() = runTest {
        //GIVEN
        val savedStateHandle = SavedStateHandle().apply {
            set(RouteParam.PRODUCT_ID, productId)
            set(RouteParam.MIN_DATE, System.currentTimeMillis())
        }
        val viewModel = CreateTransactionViewModel(
            savedStateHandle = savedStateHandle,
            createTransactionUseCase = createTransactionUseCase,
            getProductOverviewUseCase = getProductOverviewUseCase,
        )
        var message = ""

        //WHEN
        viewModel.transactionTypeInput.value = Pair(StringConstant.SELLING, TransactionEntity.OUT)
        viewModel.amountInput.value = 0.toString()
        viewModel.submit(onSuccess = {}, onError = { message = it })

        //THEN
        assert(message == StringConstant.AMOUNT_INVALID_WARNING)
    }

    @Test
    fun givenInsufficientAmount_whenCreateTransaction_amountInsufficientErrorInvoked() = runTest {
        //GIVEN
        val savedStateHandle = SavedStateHandle().apply {
            set(RouteParam.PRODUCT_ID, productId)
            set(RouteParam.MIN_DATE, System.currentTimeMillis())
        }
        val viewModel = CreateTransactionViewModel(
            savedStateHandle = savedStateHandle,
            createTransactionUseCase = createTransactionUseCase,
            getProductOverviewUseCase = getProductOverviewUseCase,
        )
        var message = ""

        //WHEN
        viewModel.transactionTypeInput.value = Pair(StringConstant.SELLING, TransactionEntity.OUT)
        viewModel.amountInput.value = 500.toString()
        viewModel.submit(onSuccess = {}, onError = { message = it })

        //THEN
        assert(message == StringConstant.AMOUNT_EXCEEDS_STOCK_WARNING)
    }
}
