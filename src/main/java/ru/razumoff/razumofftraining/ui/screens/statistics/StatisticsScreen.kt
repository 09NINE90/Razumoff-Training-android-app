package ru.razumoff.razumofftraining.ui.screens.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.razumoff.razumofftraining.R
import ru.razumoff.razumofftraining.ui.components.headers.IslandWithButtonHeader
import ru.razumoff.razumofftraining.ui.screens.statistics.components.EmptyStatisticsCard
import ru.razumoff.razumofftraining.ui.screens.statistics.components.ExerciseStatisticsCard
import ru.razumoff.razumofftraining.ui.screens.statistics.components.StatisticsPeriodSelector
import ru.razumoff.razumofftraining.ui.screens.statistics.components.StatisticsSummarySection
import ru.razumoff.razumofftraining.ui.screens.statistics.components.StatisticsWorkoutTypeSelector
import ru.razumoff.razumofftraining.ui.screens.statistics.viewmodel.StatisticsViewModel

@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel,
    onBack: () -> Unit
) {

    val summary by viewModel.summary.collectAsState()
    val exerciseStatistics by viewModel.exerciseStatistics.collectAsState()
    val period by viewModel.period.collectAsState()
    val workoutType by viewModel.workoutType.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 70.dp,
                    bottom = 24.dp
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                StatisticsPeriodSelector(
                    selectedPeriod = period,
                    onPeriodSelected = viewModel::setPeriod
                )
            }

            item {
                StatisticsWorkoutTypeSelector(
                    selectedType = workoutType,
                    onTypeSelected = viewModel::setWorkoutType
                )
            }

            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                item {
                    StatisticsSummarySection(
                        summary = summary
                    )
                }

                item {
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )
                }

                item {
                    Text(
                        text = stringResource(R.string.exercises),
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                if (exerciseStatistics.isEmpty()) {
                    item {
                        EmptyStatisticsCard()
                    }
                } else {
                    itemsIndexed(
                        items = exerciseStatistics,
                        key = { _, item -> item.exerciseId }
                    ) { index, statistic ->
                        ExerciseStatisticsCard(
                            statistic = statistic,
                            modifier = Modifier.padding(
                                bottom = if (index == exerciseStatistics.lastIndex) 110.dp else 0.dp
                            )
                        )
                    }
                }
            }
        }

        // Плавающий заголовок
        IslandWithButtonHeader(
            headerText = stringResource(R.string.statistics),
            onBackClick = onBack,
            showActionButton = false,
            showBackButton = true
        )

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(
                Alignment.BottomCenter
            )
        )
    }
}
