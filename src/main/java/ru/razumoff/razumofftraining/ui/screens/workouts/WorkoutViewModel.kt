package ru.razumoff.razumofftraining.ui.screens.workouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.razumoff.razumofftraining.models.WorkoutSession
import ru.razumoff.razumofftraining.models.SessionExercise
import ru.razumoff.razumofftraining.models.WorkoutSet
import ru.razumoff.razumofftraining.database.repository.GymRepository

class WorkoutViewModel(
    private val repository: GymRepository,
    private val userId: String
) : ViewModel() {

    private val _templatesCount = MutableStateFlow(0)
    val templatesCount: StateFlow<Int> = _templatesCount.asStateFlow()

    private val _sessions = MutableStateFlow<List<WorkoutSession>>(emptyList())
    val sessions: StateFlow<List<WorkoutSession>> = _sessions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Загружаем количество шаблонов
                val templates = repository.getTemplatesByUser(userId)
                _templatesCount.value = templates.size

                // Загружаем сессии
                val sessionEntities = repository.getLastTenSessionsByUser(userId)
                val sessions = sessionEntities.map { sessionEntity ->
                    val sessionExercises = repository.getSessionExercises(sessionEntity.id)
                    val exercises = sessionExercises.map { exerciseEntity ->
                        val sets = repository.getSetsBySessionExerciseId(exerciseEntity.id)
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
                    WorkoutSession(
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

                _sessions.value = sessions
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка загрузки данных: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refresh() {
        loadData()
    }

    class Factory(
        private val repository: GymRepository,
        private val userId: String
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WorkoutViewModel::class.java)) {
                return WorkoutViewModel(repository, userId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}