package ru.razumoff.razumofftraining.ui.screens.steps

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.health.connect.client.PermissionController
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import ru.razumoff.razumofftraining.ui.components.charts.WeeklyChart
import ru.razumoff.razumofftraining.ui.components.charts.WeeklyStepData
import ru.razumoff.razumofftraining.utils.FormatUtils
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun StepsScreen(
    viewModel: StepsViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val isConnected = viewModel.isConnected
    val stepsCount = viewModel.stepsCount
    val errorMessage = viewModel.errorMessage
    val weeklyData = viewModel.weeklyData
    val weeklyTotal = viewModel.weeklyTotal
    val weeklyAverage = viewModel.weeklyAverage
    val dailyGoal = viewModel.dailyGoal
    val isInitialLoading = viewModel.isInitialLoading
    val isRefreshing = viewModel.isRefreshing

    // Регистрируем лаунчер для запроса разрешений
    val requestPermissionsLauncher = rememberLauncherForActivityResult(
        contract = PermissionController.createRequestPermissionResultContract()
    ) { granted ->
        viewModel.onPermissionsResult(granted, context)
    }

    // Передаем лаунчер в ViewModel
    LaunchedEffect(Unit) {
        viewModel.setRequestPermissionsLauncher { permissions ->
            requestPermissionsLauncher.launch(permissions)
        }
        viewModel.checkHealthConnect(context)
    }

    // UI экрана
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        if (isInitialLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Загрузка данных...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Шапка
                if (!isConnected) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Нет связи с Health Connect",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                errorMessage?.let { errorText ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(text = "⚠️", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = errorText,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                if (isConnected) {
                    StepsCard(
                        stepsCount = stepsCount,
                        dailyGoal = dailyGoal,
                        isRefreshing = isRefreshing,
                        onRefresh = { viewModel.refreshAllData(context) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Карточка с недельной статистикой
                    WeeklyStatsCard(
                        weeklyData = weeklyData,
                        weeklyTotal = weeklyTotal,
                        weeklyAverage = weeklyAverage
                    )
                } else {
                    Button(
                        onClick = { viewModel.checkAndRequestPermissions(context) }
                    ) {
                        Text("Подключить шагомер")
                    }
                    Text(
                        text = "После нажатия откроется окно разрешений Health Connect",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}
