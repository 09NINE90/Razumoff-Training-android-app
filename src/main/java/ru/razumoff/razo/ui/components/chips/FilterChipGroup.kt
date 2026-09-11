package ru.razumoff.razo.ui.components.chips

import androidx.compose.foundation.layout.*
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> FilterChipGroup(
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    labelMapper: @Composable (T) -> String,
    chipsPerRow: Int = 4,
    chipModifier: Modifier = Modifier
) {
    val rows = items.chunked(chipsPerRow)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        rows.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowItems.forEach { item ->
                    FilterChip(
                        selected = selectedItem == item,
                        onClick = { onItemSelected(item) },
                        label = {
                            Text(
                                text = labelMapper(item),
                                maxLines = 1,
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        modifier = chipModifier
                            .weight(1f)
                            .defaultMinSize(minHeight = 36.dp)
                    )
                }
                // Добавляем пустые места для выравнивания
                repeat(chipsPerRow - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun <T> MultiSelectFilterChipGroup(
    items: List<T>,
    selectedItems: Set<T>,
    onItemToggle: (T) -> Unit,
    modifier: Modifier = Modifier,
    labelMapper: (T) -> String,
    chipsPerRow: Int = 3
) {
    val rows = items.chunked(chipsPerRow)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        rows.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowItems.forEach { item ->
                    FilterChip(
                        selected = item in selectedItems,
                        onClick = { onItemToggle(item) },
                        label = {
                            Text(
                                text = labelMapper(item),
                                maxLines = 1,
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .defaultMinSize(minHeight = 36.dp)
                    )
                }
                repeat(chipsPerRow - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}