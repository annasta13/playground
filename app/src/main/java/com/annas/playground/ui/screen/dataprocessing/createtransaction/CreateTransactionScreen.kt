package com.annas.playground.ui.screen.dataprocessing.createtransaction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import com.annas.playground.R
import com.annas.playground.data.domain.model.Product
import com.annas.playground.helper.DatePicker
import com.annas.playground.ui.components.BorderedTextField
import com.annas.playground.ui.components.OptionTextField
import com.annas.playground.ui.components.PrimaryButton
import com.annas.playground.ui.components.ProductHeaderView
import com.annas.playground.ui.components.ScreenContainer
import com.annas.playground.ui.components.Space
import com.annas.playground.ui.screen.apifetching.user.create.CreateUserContent
import com.annas.playground.ui.theme.LargePadding
import com.annas.playground.utils.ContextExtension.toast
import com.annas.playground.utils.StringExtension.isEmailValid

@Composable
fun CreateTransactionScreen(
    viewModel: CreateTransactionViewModel = hiltViewModel(),
    onSuccess: () -> Unit,
    onNavigateUp: () -> Unit
) {
    val amountInput by viewModel.amountInput
    val product by viewModel.product
    val isSubmittable = amountInput.isDigitsOnly()
    val context = LocalContext.current
    val minTransactionDate = remember { viewModel.minTransactionDate }

    CreateTransactionContent(
        timeState = viewModel.timeInput,
        transactionTypeState = viewModel.transactionTypeInput,
        amountState = viewModel.amountInput,
        isSubmittable = isSubmittable,
        minTransactionDate = minTransactionDate,
        product = product,
        stock = viewModel.stock,
        onNavigateUp = onNavigateUp,
        onSubmit = {
            viewModel.submit(onSuccess = onSuccess, onError = { context.toast(it) })
        }
    )
}


@Composable
private fun isSubmittable(timeInput: String, genderInput: String, emailInput: String): Boolean {
    val isSubmittable = timeInput.isNotEmpty()
            && genderInput.isNotEmpty()
            && emailInput.isEmailValid()
    return isSubmittable
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTransactionContent(
    product: Product?,
    stock: Int,
    timeState: MutableState<String>,
    amountState: MutableState<String>,
    transactionTypeState: MutableState<Pair<String, String>>,
    isSubmittable: Boolean,
    minTransactionDate: Long,
    onSubmit: () -> Unit,
    onNavigateUp: () -> Unit
) {
    val context = LocalContext.current
    val isDarkTheme = isSystemInDarkTheme()
    val datePicker = remember { DatePicker(context, isDarkTheme) }
    var showTransactionTypeOption by remember { mutableStateOf(false) }
    if (showTransactionTypeOption) TransactionTypeOptionSheet(
        state = transactionTypeState,
        onDismiss = { showTransactionTypeOption = false }
    )
    ScreenContainer(
        barTitle = stringResource(id = R.string.add_transaction),
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
                product?.apply {
                    ProductHeaderView(
                        name = name,
                        stock = stock,
                        image = imageUrl
                    )
                }

                BorderedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = amountState,
                    label = stringResource(id = R.string.amount),
                    inputType = KeyboardType.Number
                )

                OptionTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showTransactionTypeOption = true },
                    label = stringResource(id = R.string.transaction_type),
                    value = transactionTypeState.value.first,
                )

                OptionTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { datePicker.showDatePicker(timeState, minTransactionDate) },
                    label = stringResource(id = R.string.date),
                    state = timeState,
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
private fun CreateTransactionContentPreview() {
    val nameState = remember { mutableStateOf("") }
    val genderState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val isSubmittable = isSubmittable(
        timeInput = nameState.value,
        genderInput = genderState.value,
        emailInput = emailState.value
    )
    CreateUserContent(
        nameState = nameState,
        emailState = genderState,
        genderState = emailState,
        isSubmittable = isSubmittable,
        isLoading = true,
        onSubmit = {},
        onNavigateUp = {}
    )
}
