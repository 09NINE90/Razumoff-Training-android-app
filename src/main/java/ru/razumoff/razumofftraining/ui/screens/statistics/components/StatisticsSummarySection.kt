package ru.razumoff.razumofftraining.ui.screens.statistics.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.razumoff.razumofftraining.models.StatisticsSummary
import ru.razumoff.razumofftraining.utils.FormatUtils.formatVolume

@Composable
fun StatisticsSummarySection(
    summary: StatisticsSummary
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Общая статистика",
            style = MaterialTheme.typography.titleLarge
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            StatisticCard(
                title = "Тренировки",
                value = summary.completedSessions.toString(),
                modifier = Modifier.weight(1f)
            )

            StatisticCard(
                title = "Подходы",
                value = summary.totalSets.toString(),
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            StatisticCard(
                title = "Повторения",
                value = summary.totalReps.toString(),
                modifier = Modifier.weight(1f)
            )

            StatisticCard(
                title = "Объём",
                value = formatVolume(summary.totalVolume),
                modifier = Modifier.weight(1f)
            )
        }
    }
}