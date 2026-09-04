package ru.razumoff.razumofftraining.ui.screens.steps

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
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

//                // Значение или индикатор загрузки
                Box(
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        // Скелетон вместо крутилки
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(36.dp)
                                .height(36.dp)
                        )
                    } else {
                        Text(
                            text = stepsCount?.let { FormatUtils.formatNumberWithSpaces(it) } ?: "Нет данных",
                            fontSize = 36.sp,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }

                // Прогресс-бар
                val progress = if (stepsCount != null && dailyGoal > 0) {
                    (stepsCount.toFloat() / dailyGoal).coerceIn(0f, 1f)
                } else 0f

                Spacer(modifier = Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = when {
                        isLoading -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                        progress >= 1f -> Color.Green
                        progress >= 0.75f -> MaterialTheme.colorScheme.primary
                        progress >= 0.5f -> MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                        else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                    },
                    trackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f)
                )
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
                    tint = if (isLoading)
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    else
                        MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}