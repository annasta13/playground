package com.annas.playground.kotlin.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.annas.playground.kotlin.ui.theme.LargePadding
import com.annas.playground.kotlin.ui.theme.MediumPadding

@Composable
fun BorderedTextField(
    modifier: Modifier = Modifier,
    label: String,
    state: MutableState<String>? = null,
    focusRequester: FocusRequester? = null,
    inputType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    labelStyle: TextStyle = MaterialTheme.typography.bodySmall,
    placeholderStyle: TextStyle = MaterialTheme.typography.bodySmall,
    isMandatory: Boolean = false,
    onDone: (() -> Unit)? = null,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors().copy(
        focusedIndicatorColor = MaterialTheme.colorScheme.outline
    )
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        colors = colors,
        value = state?.value ?: "",
        onValueChange = { state?.value = it },
        readOnly = readOnly,
        label = {
            Text(
                text = if (isMandatory) label.plus("*") else label,
                style = labelStyle
            )
        },
        placeholder = {
            Text(
                text = "",
                style = placeholderStyle
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            keyboardType = inputType
        ),
        modifier = modifier.focusable(false),
        keyboardActions = KeyboardActions(
            onNext = { focusRequester?.requestFocus() },
            onDone = {
                onDone?.invoke()
                keyboardController?.hide()
            }
        ),
        enabled = enabled,
    )
}

@Composable
fun OptionTextField(
    modifier: Modifier = Modifier,
    label: String,
    isMandatory: Boolean = false,
    state: MutableState<String>? = null,
    value: String = "",
) {
    val labelUsed = if (isMandatory) label.plus("*") else label
    val showLabel = !state?.value.isNullOrEmpty() || value.isNotEmpty()
    Box(modifier = modifier) {
        Box(
            Modifier
                .defaultMinSize(
                    minWidth = OutlinedTextFieldDefaults.MinWidth,
                    minHeight = OutlinedTextFieldDefaults.MinHeight
                )
                .padding(top = MediumPadding)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outline,
                    RoundedCornerShape(4.dp)
                )
                .fillMaxWidth()
                .padding(horizontal = LargePadding),
            contentAlignment = Alignment.CenterStart
        ) {
            if (showLabel)
                Text(
                    modifier = Modifier.padding(vertical = 18.dp),
                    text = state?.value ?: value,
                    style = MaterialTheme.typography.bodyMedium
                )
            else Text(
                modifier = Modifier.padding(vertical = 20.dp),
                text = labelUsed,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
        if (showLabel) Text(
            text = labelUsed,
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 4.dp)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 4.dp),
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TextFieldPreview() {
    val state = remember { mutableStateOf("State Value") }
    Column(
//        verticalArrangement = Arrangement.spacedBy(LargePadding),
        modifier = Modifier.padding(LargePadding)
    ) {
        BorderedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = "Label 1",
            state = state
        )
        OptionTextField(
            modifier = Modifier.fillMaxWidth(),
            label = "Label 2",
            state = state
        )
        OptionTextField(
            modifier = Modifier.fillMaxWidth(),
            label = "Label 3",
            value = "Value"
        )
    }
}
