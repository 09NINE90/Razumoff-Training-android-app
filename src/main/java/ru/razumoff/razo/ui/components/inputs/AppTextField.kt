package ru.razumoff.razo.ui.components.inputs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    placeholder: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    isError: Boolean = false,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier,
        singleLine = singleLine,
        minLines = minLines,
        isError = isError,
        placeholder = placeholder?.let { { Text(it) } },
        keyboardOptions = KeyboardOptions(imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onNext = { onImeAction?.invoke() },
            onDone = { onImeAction?.invoke() }
        )
    )
}