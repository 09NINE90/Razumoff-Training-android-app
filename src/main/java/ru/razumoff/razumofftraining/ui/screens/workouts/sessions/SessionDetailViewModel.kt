package ru.razumoff.razumofftraining.ui.screens.workouts.sessions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.razumoff.razumofftraining.database.repository.GymRepository
import ru.razumoff.razumofftraining.models.SessionExercise
import ru.razumoff.razumofftraining.models.WorkoutSession
import ru.razumoff.razumofftraining.models.WorkoutSet

class SessionDetailViewModel(
    private val repository: GymRepository,
    private val userId: String,
    private val sessionId: String
) : ViewModel() {

    private val _session = MutableStateFlow<WorkoutSession?>(null)
    val session: StateFlow<WorkoutSession?> = _session.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isUpdating = MutableStateFlow(false)
    val isUpdating: StateFlow<Boolean> = _isUpdating.asStateFlow()

    private val _isDeleting = MutableStateFlow(false)
    val isDeleting: StateFlow<Boolean> = _isDeleting.asStateFlow()


    init {
        loadSession()
    }

    fun loadSession() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 1. Получаем сессию
                val sessionEntity = repository.getSessionById(sessionId, userId)
                if (sessionEntity == null) {
                    _errorMessage.value = "Тренировка не найдена"
                    return@launch
                }

                // 2. Получаем упражнения сессии
                val sessionExercises = repository.getSessionExercises(sessionId)
                val exercises = sessionExercises.map { exerciseEntity ->
                    // 3. Получаем подходы для каждого упражнения
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

                // 4. Собираем сессию
                val session = WorkoutSession(
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

                _session.value = session
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка загрузки тренировки: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateDate(newDate: Long) {
        viewModelScope.launch {
            _isUpdating.value = true
            try {
                // Обновляем дату в БД
                val currentSession = _session.value ?: return@launch
                val updatedSession = currentSession.copy(date = newDate)

                 repository.updateSessionDate(sessionId, newDate)

                _session.value = updatedSession
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка обновления даты: ${e.message}"
            } finally {
                _isUpdating.value = false
            }
        }
    }

    fun deleteSession(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isDeleting.value = true
            try {
                // 1. Получаем все упражнения сессии
                val sessionExercises = repository.getSessionExercises(sessionId)

                // 2. Удаляем подходы для каждого упражнения
                sessionExercises.forEach { exercise ->
                    repository.deleteSetsBySessionExerciseId(exercise.id)
                }

                // 3. Удаляем упражнения сессии
                sessionExercises.forEach { exercise ->
                    repository.deleteSessionExercise(exercise.id)
                }

                // 4. Удаляем саму сессию
                repository.deleteSession(sessionId, userId)

                _errorMessage.value = null
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка удаления тренировки: ${e.message}"
            } finally {
                _isDeleting.value = false
            }
        }
    }

    class Factory(
        private val repository: GymRepository,
        private val userId: String,
        private val sessionId: String
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SessionDetailViewModel::class.java)) {
                return SessionDetailViewModel(repository, userId, sessionId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
