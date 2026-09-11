package ru.razumoff.razo.ui.components.dropdown

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> MultiSelectDropdown(
    items: List<T>,
    selectedItems: Set<T>,
    onItemToggle: (T) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    label: String,
    displayMapper: @Composable (T) -> String,
    modifier: Modifier = Modifier.fillMaxWidth()
) {

    val selectedText = if (selectedItems.isEmpty()) {
        "Выберите $label"
    } else {
        buildString {
            selectedItems.forEachIndexed { index, item ->
                if (index > 0) {
                    append(", ")
                }

                append(displayMapper(item))
            }
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Checkbox(
                                checked = item in selectedItems,
                                onCheckedChange = null
                            )
                            Text(displayMapper(item))
                        }
                    },
                    onClick = { onItemToggle(item) }
                )
            }
        }
    }
}