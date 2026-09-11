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
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import ru.razumoff.razumofftraining.R
import ru.razumoff.razumofftraining.ui.theme.Success
import ru.razumoff.razumofftraining.utils.FormatUtils
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle

data class WeeklyStepData(
    val date: LocalDate,
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
    val today = LocalDate.now(ZoneId.systemDefault())

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        data.forEach { item ->

            val safeMaxValue = maxValue.coerceAtLeast(1)

            val goalProgress =
                (item.goal.toFloat() / safeMaxValue)
                    .coerceIn(0f, 1f)

            val stepsProgress =
                (item.steps.toFloat() / safeMaxValue)
                    .coerceIn(0f, 1f)

            val barColor = when {
                item.steps >= item.goal ->
                    Success

                item.steps >= item.goal * 0.75f ->
                    MaterialTheme.colorScheme.primary

                item.steps >= item.goal * 0.5f ->
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.75f)

                else ->
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)
            }

            val dayName = item.date.dayOfWeek.getDisplayName(
                TextStyle.SHORT,
                LocalLocale.current.platformLocale
            )

            val label = if (item.date == today) {
                stringResource(R.string.today)
            } else {
                stringResource(
                    R.string.day_with_number,
                    dayName,
                    item.date.dayOfMonth
                )
            }

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

                    // Цель на день
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(
                                ((item.goal.toFloat() / maxValue) * 100.dp)
                            )
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = 2.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = MaterialTheme.shapes.extraSmall
                            )
                    )

                    // Фактические шаги
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(stepsProgress * 100.dp)
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = 2.dp)
                            .background(
                                color = barColor,
                                shape = MaterialTheme.shapes.extraSmall
                            )
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // День недели
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Количество шагов
                Text(
                    text = FormatUtils.formatNumberWithSpaces(item.steps),
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
