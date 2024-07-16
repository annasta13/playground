package com.annas.playground.ui.ui.screen.dataprocessing.productlist

import com.annas.playground.data.domain.usecase.getproductlist.GetProductList
import com.annas.playground.data.domain.usecase.getproductlist.GetProductListImpl
import com.annas.playground.data.local.dao.ProductDao
import com.annas.playground.data.repository.product.ProductRepository
import com.annas.playground.data.repository.product.ProductRepositoryImpl
import com.annas.playground.helper.DataHelper
import com.annas.playground.ui.helper.BaseTest
import com.annas.playground.ui.screen.dataprocessing.productlist.ProductListViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.doReturn

class ProductListViewModelTest : BaseTest() {
    private lateinit var dataHelper: DataHelper
    private lateinit var getProductList: GetProductList
    private lateinit var dao: ProductDao

    @Before
    fun setup() {
        dataHelper = DataHelper()
        dao = Mockito.mock()
        val repository: ProductRepository = ProductRepositoryImpl(dao, dataHelper)
        getProductList = GetProductListImpl(repository)
    }

    @Test
    fun givenDao_whenViewModelInitialized_productListReturned() = runTest {
        //GIVEN
        val mockProductList = dataHelper.getMockProducts()
        Mockito.`when`(dao.getProductList()) doReturn mockProductList

        //WHEN
        val viewModel = ProductListViewModel(getProductList)

        assert(viewModel.list.isNotEmpty())
    }
}
