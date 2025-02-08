package com.annas.playground.ui.main

import com.annas.playground.kotlin.data.domain.usecase.prepareasset.PrepareInitialDataUseCaseImpl
import com.annas.playground.kotlin.data.domain.usecase.prepareasset.PrepareMockDataUseCase
import com.annas.playground.kotlin.data.local.dao.ProductDao
import com.annas.playground.kotlin.data.local.dao.TransactionDao
import com.annas.playground.kotlin.data.repository.product.ProductRepository
import com.annas.playground.kotlin.data.repository.product.ProductRepositoryImpl
import com.annas.playground.kotlin.data.repository.transaction.TransactionRepository
import com.annas.playground.kotlin.data.repository.transaction.TransactionRepositoryImpl
import com.annas.playground.kotlin.helper.common.DataHelper
import com.annas.playground.kotlin.ui.activity.MainViewModel
import com.annas.playground.ui.helper.BaseTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.times

class MainViewModelTest : BaseTest() {

    private lateinit var transactionDao: TransactionDao
    private lateinit var productDao: ProductDao
    private lateinit var dataHelper: DataHelper
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        transactionDao = Mockito.mock()
        productDao = Mockito.mock()
        dataHelper = DataHelper()
        val productRepository: ProductRepository = ProductRepositoryImpl(productDao, dataHelper)
        val transactionRepository: TransactionRepository =
            TransactionRepositoryImpl(transactionDao, dataHelper)
        val prepareDataUseCase: PrepareMockDataUseCase =
            PrepareInitialDataUseCaseImpl(productRepository, transactionRepository)
        viewModel = MainViewModel(prepareDataUseCase)
    }

    @Test
    fun `prepare product should success`() = runTest {
        //GIVEN
        Mockito.`when`(transactionDao.countTransaction()) doReturn (0)
        Mockito.`when`(productDao.countProduct()) doReturn (0)
        Mockito.`when`(productDao.insertProducts(Mockito.anyList())).thenReturn(Unit)
        Mockito.`when`(transactionDao.insertTransactions(Mockito.anyList()))
            .thenReturn(Unit)

        //WHEN
        viewModel.prepareProduct()

        //THEN
        Mockito.verify(productDao).insertProducts(Mockito.anyList())
        Mockito.verify(transactionDao, times(2)).insertTransactions(Mockito.anyList())
    }
}
