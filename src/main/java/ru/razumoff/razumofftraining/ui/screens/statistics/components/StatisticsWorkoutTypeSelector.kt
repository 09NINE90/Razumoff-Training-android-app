package ru.razumoff.razumofftraining.ui.screens.statistics.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import ru.razumoff.razumofftraining.R
import ru.razumoff.razumofftraining.models.StatisticsWorkoutType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsWorkoutTypeSelector(
    selectedType: StatisticsWorkoutType,
    onTypeSelected: (StatisticsWorkoutType) -> Unit
) {
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        StatisticsWorkoutType.entries.forEachIndexed { index, type ->

            val active = selectedType == type

            SegmentedButton(
                selected = active,
                onClick = {
                    onTypeSelected(type)
                },
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = StatisticsWorkoutType.entries.size
                ),
                icon = {
                    SegmentedButtonDefaults.Icon(
                        active = active,
                        activeContent = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_check),
                                contentDescription = null
                            )
                        },
                        inactiveContent = {}
                    )
                }
            ) {
                Text(type.displayName)
            }
        }
    }
}