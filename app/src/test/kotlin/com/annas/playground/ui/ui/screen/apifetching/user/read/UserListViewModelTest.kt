package com.annas.playground.ui.ui.screen.apifetching.user.read

import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.testing.ErrorRecovery
import androidx.paging.testing.asSnapshot
import com.annas.playground.data.domain.model.User
import com.annas.playground.data.domain.usecase.getuser.GetUserUseCase
import com.annas.playground.data.domain.usecase.getuser.GetUserUseCaseImpl
import com.annas.playground.data.remote.builder.ApiService
import com.annas.playground.data.remote.model.UserResponse
import com.annas.playground.data.repository.user.UserRepository
import com.annas.playground.data.repository.user.UserRepositoryImpl
import com.annas.playground.ui.helper.BaseTest
import com.annas.playground.ui.screen.apifetching.user.read.UserListViewModel
import com.annas.playground.utils.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import retrofit2.Response
import java.net.HttpURLConnection


class UserListViewModelTest : BaseTest() {
    private lateinit var viewModel: UserListViewModel
    private lateinit var service: ApiService
    private lateinit var getUserUseCase: GetUserUseCase

    @Before
    fun setup() {
        service = Mockito.mock()
        val repository: UserRepository = UserRepositoryImpl(service, Dispatchers.Unconfined)
        getUserUseCase = GetUserUseCaseImpl(repository)
    }

    @Test
    fun givenMockService_whenViewModelGetUser_thenMockDataReturned() = runTest {
        //GIVEN
        val source =
            JsonParser.getMockList("user.json", Array<UserResponse>::class.java)!!.toList()
        val response = Response.success(HttpURLConnection.HTTP_OK, source)
        Mockito.`when`(service.getUser(1)) doReturn (response)

        //WHEN
        viewModel = UserListViewModel(getUserUseCase)

        val items: Flow<PagingData<User>> = viewModel.list
        val snapshot: List<User> = items.asSnapshot()

        //THEN
        assert(snapshot[0].name.isNotEmpty())
        assert(snapshot.isNotEmpty())
    }

    @Test
    fun givenMockService_whenViewModelGetUser_thenExceptionThrown() = runTest {

        //GIVEN
        doThrow(IllegalStateException("Error occurred")).`when`(service)
            .getUser(1)
        var loadState: LoadState? = null

        //WHEN
        viewModel = UserListViewModel(getUserUseCase)
        val items: Flow<PagingData<User>> = viewModel.list

        //THEN
        assertThrows(IllegalStateException::class.java) {
            runBlocking {
                items.asSnapshot(onError = {
                    loadState = it.refresh
                    ErrorRecovery.THROW
                })
                service.getUser(1)
            }
        }
        assert(loadState is LoadState.Error)
    }
}
