package ru.razumoff.razumofftraining.ui.components.charts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import ru.razumoff.razumofftraining.utils.FormatUtils

data class WeeklyStepData(
    val day: String,
    val steps: Int,
    val goal: Int = 10000 // цель на день
)

// Компонент графика
@Composable
fun WeeklyChart(
    data: List<WeeklyStepData>,
    maxValue: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        data.forEach { item ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                // Столбец
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp)
                ) {
                    // Фоновый столбец (цель)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(
                                ((item.goal.toFloat() / maxValue) * 100.dp)
                            )
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = 2.dp)
                            .background(
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                                shape = MaterialTheme.shapes.extraSmall
                            )
                    )

                    // Основной столбец (шаги)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(
                                ((item.steps.toFloat() / maxValue) * 100.dp)
                            )
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = 2.dp)
                            .background(
                                color = if (item.steps >= item.goal)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                                shape = MaterialTheme.shapes.extraSmall
                            )
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // День недели
                Text(
                    text = item.day,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Количество шагов
                Text(
                    text = FormatUtils.formatNumberWithSpaces(item.steps),
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}
