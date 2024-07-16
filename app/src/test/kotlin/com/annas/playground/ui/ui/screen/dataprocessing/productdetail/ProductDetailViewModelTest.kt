package com.annas.playground.ui.ui.screen.dataprocessing.productdetail

import androidx.lifecycle.SavedStateHandle
import com.annas.playground.data.domain.usecase.getproductdetail.GetProductDetail
import com.annas.playground.data.domain.usecase.getproductdetail.GetProductDetailImpl
import com.annas.playground.data.local.dao.ProductDao
import com.annas.playground.data.local.dao.TransactionDao
import com.annas.playground.data.local.model.TransactionEntity
import com.annas.playground.data.repository.product.ProductRepository
import com.annas.playground.data.repository.product.ProductRepositoryImpl
import com.annas.playground.data.repository.transaction.TransactionRepository
import com.annas.playground.data.repository.transaction.TransactionRepositoryImpl
import com.annas.playground.helper.DataHelper
import com.annas.playground.ui.graph.RouteParam
import com.annas.playground.ui.helper.BaseTest
import com.annas.playground.ui.screen.dataprocessing.productdetail.ProductDetailViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.doReturn

class ProductDetailViewModelTest : BaseTest() {
    private lateinit var dataHelper: DataHelper
    private lateinit var getProductDetail: GetProductDetail
    private lateinit var productDao: ProductDao
    private lateinit var transactionDao: TransactionDao

    @Before
    fun setup() {
        dataHelper = DataHelper()
        productDao = Mockito.mock()
        transactionDao = Mockito.mock()
        val productRepository: ProductRepository = ProductRepositoryImpl(productDao, dataHelper)
        val transactionRepository: TransactionRepository =
            TransactionRepositoryImpl(transactionDao, dataHelper)
        getProductDetail = GetProductDetailImpl(productRepository, transactionRepository)
    }

    @Test
    fun givenDao_whenViewModelInitialized_productDetailReturned() = runTest {
        //GIVEN
        val productId = 1
        val mockProductList = dataHelper.getMockProducts().first { it.id == productId }
        val mockTransactionList = dataHelper.getMockTransactions(TransactionEntity.OUT)
        val savedStateHandle = SavedStateHandle().apply { set(RouteParam.PRODUCT_ID, productId) }

        //WHEN
        Mockito.`when`(productDao.getProductById(productId)) doReturn mockProductList
        Mockito.`when`(transactionDao.getTransactionOutByProductId(productId)) doReturn 1230
        Mockito.`when`(transactionDao.getTransactionInByProductId(productId)) doReturn 120
        Mockito.`when`(transactionDao.getTransactionsByProductId(productId)) doReturn mockTransactionList
        val viewModel = ProductDetailViewModel(getProductDetail, savedStateHandle)

        assert(viewModel.product.value != null)
        assert(viewModel.product.value!!.id == productId)
        assert(viewModel.product.value!!.transactionList.isNotEmpty())
    }
}
