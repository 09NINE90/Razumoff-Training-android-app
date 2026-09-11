package ru.razumoff.razo.ui.screens.workouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.razumoff.razo.database.entities.WorkoutSessionEntity
import ru.razumoff.razo.database.repository.GymRepository
import ru.razumoff.razo.models.SessionExercise
import ru.razumoff.razo.models.WorkoutSession
import ru.razumoff.razo.models.WorkoutSet
import kotlin.collections.emptyList
import kotlin.time.Duration.Companion.milliseconds

class WorkoutViewModel(
    private val repository: GymRepository,
    private val userId: String
) : ViewModel() {

    val templatesCount: StateFlow<Int> =
        repository
            .observeTemplatesCount(userId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0
            )

    val sessions: StateFlow<List<WorkoutSession>> =
        repository
            .observeLastTenSessionsByUser(userId)
            .map { sessionEntities ->
                sessionEntities.map { sessionEntity ->
                    loadWorkoutSession(sessionEntity)
                }
            }
            .onEach {
                _isInitialLoading.value = false
            }
            .catch { e ->
                _errorMessage.value =
                    "Ошибка загрузки данных: ${e.message}"

                _isInitialLoading.value = false

                emit(emptyList())
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )


    // Показываем полный LoadingScreen только при первой загрузке.
    private val _isInitialLoading = MutableStateFlow(true)
    val isInitialLoading: StateFlow<Boolean> =
        _isInitialLoading.asStateFlow()

    // Отдельное состояние для ручного обновления.
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> =
        _isRefreshing.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> =
        _errorMessage.asStateFlow()


    private suspend fun loadWorkoutSession(
        sessionEntity: WorkoutSessionEntity
    ): WorkoutSession {

        val sessionExercises =
            repository.getSessionExercises(sessionEntity.id)

        val exercises = sessionExercises.map { exerciseEntity ->

            val sets =
                repository.getSetsBySessionExerciseId(
                    exerciseEntity.id
                )

            val workoutSets = sets.map { setEntity ->
                WorkoutSet(
                    id = setEntity.id,
                    sessionExerciseId = setEntity.sessionExerciseId,
                    setNumber = setEntity.setNumber,
                    reps = setEntity.reps,
                    weight = setEntity.weight,
                    isWarmup = setEntity.isWarmup,
                    createdAt = setEntity.createdAt
                )
            }

            SessionExercise(
                id = exerciseEntity.id,
                sessionId = exerciseEntity.sessionId,
                templateExerciseId = exerciseEntity.templateExerciseId,
                exerciseId = exerciseEntity.exerciseId,
                exerciseName = exerciseEntity.exerciseName,
                order = exerciseEntity.order,
                sets = workoutSets,
                notes = exerciseEntity.notes
            )
        }

        return WorkoutSession(
            id = sessionEntity.id,
            templateId = sessionEntity.templateId,
            templateName = sessionEntity.templateName,
            date = sessionEntity.date,
            exercises = exercises,
            duration = sessionEntity.duration,
            notes = sessionEntity.notes,
            feeling = sessionEntity.feeling,
            isCompleted = sessionEntity.isCompleted
        )
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true

            try {
                // Room Flow сам обновит данные.
                // Здесь не нужен delay().
            } finally {
                _isRefreshing.value = false
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
            if (modelClass.isAssignableFrom(WorkoutViewModel::class.java)) {
                return WorkoutViewModel(
                    repository,
                    userId
                ) as T
            }

            throw IllegalArgumentException(
                "Unknown ViewModel class"
            )
        }
    }
}
