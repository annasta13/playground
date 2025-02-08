package com.annas.playground.kotlin.ui.screen.apifetching.user.create

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.annas.playground.R
import com.annas.playground.kotlin.ui.components.BorderedTextField
import com.annas.playground.kotlin.ui.components.OptionTextField
import com.annas.playground.kotlin.ui.components.PrimaryButton
import com.annas.playground.kotlin.ui.components.ScreenContainer
import com.annas.playground.kotlin.ui.components.Space
import com.annas.playground.kotlin.ui.components.ThemePreviewParameterProvider
import com.annas.playground.kotlin.ui.theme.LargePadding
import com.annas.playground.kotlin.ui.theme.PlaygroundTheme
import com.annas.playground.kotlin.utils.ContextExtension.toast
import com.annas.playground.kotlin.utils.StringExtension.isEmailValid

@Composable
fun CreateUserScreen(
    viewModel: CreateUserViewModel = hiltViewModel(),
    onSuccess: () -> Unit,
    onNavigateUp: () -> Unit
) {
    val isLoading by viewModel.isLoading
    val nameInput by viewModel.nameInput
    val emailInput by viewModel.emailInput
    val genderInput by viewModel.genderInput
    val isSubmittable = isSubmittable(nameInput, genderInput, emailInput)

    val context = LocalContext.current
    CreateUserContent(
        nameState = viewModel.nameInput,
        emailState = viewModel.emailInput,
        genderState = viewModel.genderInput,
        isSubmittable = isSubmittable,
        isLoading = isLoading,
        onNavigateUp = onNavigateUp,
        onSubmit = {
            viewModel.submit(
                onSuccess = onSuccess,
                onError = { context.toast(it) }
            )
        }
    )
}

@Composable
private fun isSubmittable(nameInput: String, genderInput: String, emailInput: String): Boolean {
    val isSubmittable = nameInput.isNotEmpty()
            && genderInput.isNotEmpty()
            && emailInput.isEmailValid()
    return isSubmittable
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUserContent(
    nameState: MutableState<String>,
    emailState: MutableState<String>,
    genderState: MutableState<String>,
    isSubmittable: Boolean,
    isLoading: Boolean,
    onSubmit: () -> Unit,
    onNavigateUp: () -> Unit
) {
    var showGenderOptions by remember { mutableStateOf(false) }
    if (showGenderOptions) GenderOptionSheet(
        state = genderState,
        onDismiss = { showGenderOptions = false }
    )
    ScreenContainer(
        barTitle = stringResource(id = R.string.add_user),
        isTransparentLoading = isLoading,
        onLeadingIconClicked = onNavigateUp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(LargePadding)
            ) {
                BorderedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = nameState,
                    label = stringResource(id = R.string.name)
                )
                BorderedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = emailState,
                    label = stringResource(id = R.string.email),
                    inputType = KeyboardType.Email
                )
                OptionTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showGenderOptions = true },
                    label = stringResource(id = R.string.gender),
                    state = genderState,
                )
                Space(size = 25.dp)
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isSubmittable,
                    text = stringResource(id = R.string.save),
                    onClick = onSubmit
                )
            }
        }
    }
}

@Preview
@Composable
private fun CreateUserContentPreview(@PreviewParameter(ThemePreviewParameterProvider::class) isDarkTheme: Boolean) {
    val nameState = remember { mutableStateOf("") }
    val genderState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val isSubmittable = isSubmittable(
        nameInput = nameState.value,
        genderInput = genderState.value,
        emailInput = emailState.value
    )
    PlaygroundTheme(isDarkTheme) {
        CreateUserContent(
            nameState = nameState,
            emailState = genderState,
            genderState = emailState,
            isSubmittable = isSubmittable,
            isLoading = false,
            onSubmit = {},
            onNavigateUp = {}
        )
    }
}
