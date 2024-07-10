package com.annas.playground.ui.screen.apifetching.user.create

import androidx.compose.runtime.mutableStateOf
import com.annas.playground.data.domain.usecase.postuser.PostUserUseCase
import com.annas.playground.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class CreateUserViewModel @Inject constructor(private val postUserUseCase: PostUserUseCase) :
    BaseViewModel() {

    var count = Random.nextInt(from = 1, until = 10)
    var nameInput = mutableStateOf("")
        private set
    var genderInput = mutableStateOf("")
        private set
    var emailInput = mutableStateOf("")
        private set

    fun submit(onSuccess: () -> Unit, onError: (String) -> Unit) = execute {
        postUserUseCase.invoke(
            email = emailInput.value.trim(),
            name = nameInput.value.trim(),
            gender = genderInput.value.lowercase(),
            status = "active"
        )
            .onStart { showLoading() }
            .collect { result ->
                result.onSuccess { onSuccess.invoke() }
                    .onFailure {
                        if (!it.message.isNullOrEmpty())
                            onError.invoke(it.message.orEmpty())
                    }
            }
    }
}
