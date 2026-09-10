package ru.razumoff.razumofftraining.ui.screens.statistics.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.razumoff.razumofftraining.database.repository.GymRepository
import ru.razumoff.razumofftraining.models.ExerciseStatistics
import ru.razumoff.razumofftraining.models.StatisticsPeriod
import ru.razumoff.razumofftraining.models.StatisticsSummary
import ru.razumoff.razumofftraining.models.StatisticsWorkoutType
import java.time.LocalDate
import java.time.ZoneId

class StatisticsViewModel(
    private val repository: GymRepository,
    private val userId: String
) : ViewModel() {

    private val _period = MutableStateFlow(StatisticsPeriod.ALL_TIME)
    val period: StateFlow<StatisticsPeriod> = _period.asStateFlow()

    private val _workoutType = MutableStateFlow(StatisticsWorkoutType.ALL)
    val workoutType: StateFlow<StatisticsWorkoutType> =
        _workoutType.asStateFlow()

    private val _summary = MutableStateFlow(StatisticsSummary())
    val summary: StateFlow<StatisticsSummary> = _summary.asStateFlow()

    private val _exerciseStatistics =
        MutableStateFlow<List<ExerciseStatistics>>(emptyList())

    val exerciseStatistics: StateFlow<List<ExerciseStatistics>> =
        _exerciseStatistics.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadStatistics()
    }

    fun setPeriod(period: StatisticsPeriod) {
        if (_period.value == period) return

        _period.value = period
        loadStatistics()
    }

    fun setWorkoutType(type: StatisticsWorkoutType) {
        if (_workoutType.value == type) return

        _workoutType.value = type
        loadStatistics()
    }

    fun refresh() {
        loadStatistics()
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val workoutType = _workoutType.value.toWorkoutType()
                val fromDate = getFromDate()

                kotlinx.coroutines.coroutineScope {
                    val sessionsDeferred = async {
                        repository.getCompletedSessionsCount(
                            userId,
                            workoutType,
                            fromDate
                        )
                    }

                    val setsDeferred = async {
                        repository.getTotalSets(
                            userId,
                            workoutType,
                            fromDate
                        )
                    }

                    val repsDeferred = async {
                        repository.getTotalReps(
                            userId,
                            workoutType,
                            fromDate
                        )
                    }

                    val volumeDeferred = async {
                        repository.getTotalVolume(
                            userId,
                            workoutType,
                            fromDate
                        )
                    }

                    val exercisesDeferred = async {
                        repository.getExerciseStatistics(
                            userId,
                            workoutType,
                            fromDate
                        )
                    }

                    _summary.value = StatisticsSummary(
                        completedSessions = sessionsDeferred.await(),
                        totalSets = setsDeferred.await(),
                        totalReps = repsDeferred.await(),
                        totalVolume = volumeDeferred.await()
                    )

                    _exerciseStatistics.value =
                        exercisesDeferred.await().map { row ->
                            ExerciseStatistics(
                                exerciseId = row.exerciseId,
                                exerciseName = row.exerciseName,
                                maxWeight = row.maxWeight,
                                totalVolume = row.totalVolume,
                                totalSets = row.totalSets,
                                totalReps = row.totalReps,
                                sessionsCount = row.sessionsCount
                            )
                        }
                }
            } catch (e: Exception) {
                _errorMessage.value =
                    "Ошибка загрузки статистики: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun getFromDate(): Long? {
        return when (_period.value) {
            StatisticsPeriod.ALL_TIME -> null

            StatisticsPeriod.THIS_MONTH -> {
                LocalDate.now()
                    .withDayOfMonth(1)
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
            }
        }
    }

    class Factory(
        private val repository: GymRepository,
        private val userId: String
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>
        ): T {
            if (modelClass.isAssignableFrom(StatisticsViewModel::class.java)) {
                return StatisticsViewModel(
                    repository = repository,
                    userId = userId
                ) as T
            }

            throw IllegalArgumentException(
                "Unknown ViewModel class: ${modelClass.name}"
            )
        }
    }
}
