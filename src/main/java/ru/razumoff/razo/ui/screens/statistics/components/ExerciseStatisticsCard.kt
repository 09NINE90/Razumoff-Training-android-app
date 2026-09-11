package ru.razumoff.razo.ui.screens.statistics.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.razumoff.razo.R
import ru.razumoff.razo.models.ExerciseStatistics
import ru.razumoff.razo.utils.FormatUtils.formatVolume

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
                    "${statistic.maxWeight} ${stringResource(R.string.kg)}"
                } else {
                    stringResource(R.string.without_weight)
                },
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = if (statistic.maxWeight > 0f) {
                    stringResource(R.string.max_weight)
                } else {
                    stringResource(R.string.bodyweight_exercise)
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
                    title = stringResource(R.string.sets),
                    value = statistic.totalSets.toString()
                )

                StatisticValue(
                    title = stringResource(R.string.repetitions),
                    value = statistic.totalReps.toString()
                )

                StatisticValue(
                    title = stringResource(R.string.workouts),
                    value = statistic.sessionsCount.toString()
                )
            }

            if (statistic.totalVolume > 0) {
                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatisticValue(
                        title = stringResource(R.string.volume),
                        value = formatVolume(statistic.totalVolume)
                    )
                }
            }
        }
    }
}
