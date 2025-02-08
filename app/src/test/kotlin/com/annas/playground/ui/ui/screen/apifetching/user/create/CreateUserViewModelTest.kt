package com.annas.playground.ui.ui.screen.apifetching.user.create

import com.annas.playground.kotlin.data.domain.usecase.postuser.PostUserUseCase
import com.annas.playground.kotlin.data.domain.usecase.postuser.PostUserUseCaseImpl
import com.annas.playground.kotlin.data.remote.builder.ApiService
import com.annas.playground.kotlin.data.remote.model.UserRequest
import com.annas.playground.kotlin.data.remote.model.UserResponse
import com.annas.playground.kotlin.data.repository.user.UserRepository
import com.annas.playground.kotlin.data.repository.user.UserRepositoryImpl
import com.annas.playground.ui.helper.BaseTest
import com.annas.playground.kotlin.ui.screen.apifetching.user.create.CreateUserViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.doThrow

class CreateUserViewModelTest : BaseTest() {

    private lateinit var service: ApiService
    private lateinit var viewModel: CreateUserViewModel

    @Before
    fun setup() {
        service = Mockito.mock()
        val repository: UserRepository = UserRepositoryImpl(service, dispatcher)
        val postUserUseCase: PostUserUseCase = PostUserUseCaseImpl(repository)
        viewModel = CreateUserViewModel(postUserUseCase)
    }

    private val email = "test_sample@example.com"
    private val name = "Test Sample"
    private val gender = "male"

    @Test
    fun givenMockApiService_whenViewModelSubmit_thenUserResponseReturned() = runTest {
        //GIVEN
        val response = UserResponse(email, gender, 1, name = name, status = "active")
        var success = false

        //WHEN
        `when`(service.postUser(request = anyMock(UserRequest::class.java))).thenReturn(response)
        setInputValue()

        viewModel.submit(onSuccess = { success = true }, onError = {})

        //THEN
        assert(success)
    }

    private fun setInputValue() {
        viewModel.nameInput.value = name
        viewModel.emailInput.value = email
        viewModel.genderInput.value = gender
    }

    @Test
    fun givenMockApiService_whenViewModelSubmit_thenExceptionThrown() = runTest {
        //GIVEN
        var error = false
        val request = anyMock(UserRequest::class.java)
        `when`(service.postUser(request)) doThrow (NullPointerException("Error occurred"))

        //WHEN
        setInputValue()
        viewModel.submit(onSuccess = {}, onError = { error = true })
        //THEN
        assert(error)
    }
}
