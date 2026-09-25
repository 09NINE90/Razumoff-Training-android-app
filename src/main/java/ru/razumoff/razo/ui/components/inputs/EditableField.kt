package ru.razumoff.razo.ui.components.inputs

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EditableField(
    label: String,
    value: String,
    isEditing: Boolean,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    singleLine: Boolean = true,
    displayValue: String = value,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    AnimatedContent(
        targetState = isEditing,
        modifier = modifier.fillMaxWidth(),
        transitionSpec = {
            if (targetState) {
                scaleIn() + fadeIn() togetherWith
                        scaleOut() + fadeOut()
            } else {
                scaleIn() + fadeIn() togetherWith
                        scaleOut() + fadeOut()
            }
        },
        label = "editableField"
    ) { editing ->

        if (editing) {

            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text(label) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = singleLine,
                placeholder = placeholder?.let {
                    { Text(it) }
                },
                trailingIcon = trailingIcon?.let {
                    { it() }
                }
            )

        } else {

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = displayValue,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}