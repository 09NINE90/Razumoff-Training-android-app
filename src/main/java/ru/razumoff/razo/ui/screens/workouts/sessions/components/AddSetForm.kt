package ru.razumoff.razo.ui.screens.workouts.sessions.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ru.razumoff.razo.R
import ru.razumoff.razo.ui.components.cards.SurfaceCard

@Composable
fun AddSetForm(
    repsInput: String,
    onRepsChange: (String) -> Unit,
    weightInput: String,
    onWeightChange: (String) -> Unit,
    onAddSet: () -> Unit,
    isEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    SurfaceCard(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.add_set),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = repsInput,
                    onValueChange = onRepsChange,
                    label = { Text("Повторения") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    isError = repsInput.isNotEmpty() && repsInput.toIntOrNull() == null,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                OutlinedTextField(
                    value = weightInput,
                    onValueChange = onWeightChange,
                    label = { Text(stringResource(R.string.weight_label)) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onAddSet,
                modifier = Modifier.fillMaxWidth(),
                enabled = isEnabled
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.add_set))
            }
        }
    }
}
