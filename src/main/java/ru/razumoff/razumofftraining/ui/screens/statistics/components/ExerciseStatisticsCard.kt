package ru.razumoff.razumofftraining.ui.screens.statistics.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.razumoff.razumofftraining.models.ExerciseStatistics
import ru.razumoff.razumofftraining.utils.FormatUtils.formatVolume
@Composable
fun ExerciseStatisticsCard(
    statistic: ExerciseStatistics,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = statistic.exerciseName,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = if (statistic.maxWeight > 0f) {
                    "${statistic.maxWeight} кг"
                } else {
                    "Без веса"
                },
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = if (statistic.maxWeight > 0f) {
                    "максимальный вес"
                } else {
                    "упражнение без веса"
                },
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            HorizontalDivider()

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatisticValue(
                    title = "Подходы",
                    value = statistic.totalSets.toString()
                )

                StatisticValue(
                    title = "Повторения",
                    value = statistic.totalReps.toString()
                )

                StatisticValue(
                    title = "Тренировки",
                    value = statistic.sessionsCount.toString()
                )
            }

            if (statistic.totalVolume > 0){
                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatisticValue(
                        title = "Объём",
                        value = formatVolume(statistic.totalVolume)
                    )
                }
            }
        }
    }
}