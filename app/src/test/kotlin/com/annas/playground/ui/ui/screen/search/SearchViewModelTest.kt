package com.annas.playground.ui.ui.screen.search

import com.annas.playground.data.domain.usecase.getcity.GetCityUseCase
import com.annas.playground.data.domain.usecase.getcity.GetCityUseCaseImpl
import com.annas.playground.data.repository.city.CityRepository
import com.annas.playground.data.repository.city.CityRepositoryImpl
import com.annas.playground.ui.helper.BaseTest
import com.annas.playground.ui.screen.search.SearchViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SearchViewModelTest : BaseTest() {
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        val repository: CityRepository = CityRepositoryImpl()
        val useCase: GetCityUseCase = GetCityUseCaseImpl(repository)
        viewModel = SearchViewModel(useCase)
    }

    @Test
    fun givenCityList_whenViewModelSearchDefault_allCityListCollected() = runTest {
        //GIVEN
        val searchKey = ""
        viewModel.searchKey.value = searchKey

        //WHEN
        viewModel.searchCity()

        //THEN
        assert(viewModel.listShowing.isNotEmpty())
    }

    @Test
    fun givenCityList_whenViewModelSearchWithKeyword_listShowingFilterContainsKeyword() = runTest {
        //GIVEN
        viewModel.searchCity()
        val searchKey = "ci"
        viewModel.searchKey.value = searchKey

        //WHEN
        viewModel.searchCity()

        //THEN
        assert(viewModel.listShowing.isNotEmpty())
        viewModel.listShowing.forEach {
            assert(it.contains(searchKey, true))
        }
    }
}
