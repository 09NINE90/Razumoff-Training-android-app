package ru.razumoff.razumofftraining.ui.screens.steps

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.razumoff.razumofftraining.ui.theme.Success
import ru.razumoff.razumofftraining.utils.FormatUtils

@Composable
fun StepsCard(
    stepsCount: Long?,
    dailyGoal: Int,
    isLoading: Boolean = false,
    onRefresh: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Box(
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Основной контент
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Заголовок
                Text(
                    text = "Сегодня",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Значение или индикатор загрузки
                Box(
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading || stepsCount == null) {
                        Box(
                            modifier = Modifier
                                .width(120.dp)
                                .height(36.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(6.dp)
                                )
                        )
                    } else {
                        Text(
                            text = FormatUtils.formatNumberWithSpaces(stepsCount),
                            fontSize = 36.sp,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }

                // Прогресс-бар
                val progress = if (
                    stepsCount != null && dailyGoal > 0
                ) {
                    (stepsCount.toFloat() / dailyGoal)
                        .coerceIn(0f, 1f)
                } else {
                    0f
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                } else {
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = when {
                            progress >= 1f -> Success
                            progress >= 0.75f ->
                                MaterialTheme.colorScheme.primary
                            progress >= 0.5f ->
                                MaterialTheme.colorScheme.primary
                                    .copy(alpha = 0.75f)
                            else ->
                                MaterialTheme.colorScheme.primary
                                    .copy(alpha = 0.45f)
                        },
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }

            // Кнопка обновления
            IconButton(
                onClick = onRefresh,
                enabled = !isLoading,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Обновить",
                    modifier = Modifier.size(20.dp),
                    tint = if (isLoading) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                            .copy(alpha = 0.5f)
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )
            }
        }
    }
}