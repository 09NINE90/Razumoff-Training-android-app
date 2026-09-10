package ru.razumoff.razumofftraining.ui.screens.workouts.sessions

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.razumoff.razumofftraining.database.entities.SessionExerciseEntity
import ru.razumoff.razumofftraining.database.entities.WorkoutSessionEntity
import ru.razumoff.razumofftraining.database.entities.WorkoutSetEntity
import ru.razumoff.razumofftraining.database.repository.GymRepository
import java.util.UUID

data class SessionExerciseState(
    val sessionExerciseId: String,
    val exerciseId: String,
    val exerciseName: String,
    val order: Int,
    val sets: List<WorkoutSetEntity> = emptyList()
)

data class WorkoutSessionState(
    val sessionId: String = "",
    val templateName: String = "",
    val exercises: List<SessionExerciseState> = emptyList(),
    val currentExerciseIndex: Int = 0,
    val isCompleted: Boolean = false,
    val duration: Int = 0 // в секундах
)

class WorkoutSessionViewModel(
    private val repository: GymRepository,
    private val userId: String,
    private val templateId: String
) : ViewModel() {

    private val _state = MutableStateFlow(WorkoutSessionState())
    val state: StateFlow<WorkoutSessionState> = _state.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private var timerJob: Job? = null

    init {
        startSession()
    }

    private fun startSession() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 1. Получаем шаблон
                val template = repository.getTemplateById(templateId, userId)
                if (template == null) {
                    _errorMessage.value = "Шаблон не найден"
                    return@launch
                }

                // 2. Получаем упражнения шаблона
                val templateExercises = repository.getTemplateExercises(templateId)
                if (templateExercises.isEmpty()) {
                    _errorMessage.value = "В шаблоне нет упражнений"
                    return@launch
                }

                // 3. Создаем сессию
                val sessionId = UUID.randomUUID().toString()
                val sessionEntity = WorkoutSessionEntity(
                    id = sessionId,
                    userId = userId,
                    templateId = templateId,
                    templateName = template.name,
                    workoutType = template.workoutType,
                    isCompleted = false
                )
                repository.insertSession(sessionEntity)

                // 4. Создаем записи для упражнений
                val sessionExercises = templateExercises.mapIndexed { index, exercise ->
                    val exerciseEntity = repository.getExerciseById(exercise.exerciseId, userId)
                    SessionExerciseEntity(
                        id = UUID.randomUUID().toString(),
                        sessionId = sessionId,
                        templateExerciseId = exercise.id,
                        exerciseId = exercise.exerciseId,
                        exerciseName = exerciseEntity?.name ?: "Неизвестное упражнение",
                        order = index + 1
                    )
                }
                repository.insertSessionExercises(sessionExercises)

                // 5. Формируем состояние
                val exerciseStates = sessionExercises.map { sessionExercise ->
                    SessionExerciseState(
                        sessionExerciseId = sessionExercise.id,
                        exerciseId = sessionExercise.exerciseId,
                        exerciseName = sessionExercise.exerciseName,
                        order = sessionExercise.order,
                        sets = emptyList()
                    )
                }

                _state.value = WorkoutSessionState(
                    sessionId = sessionId,
                    templateName = template.name,
                    exercises = exerciseStates,
                    currentExerciseIndex = 0
                )

                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка начала тренировки: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getCurrentExercise(): SessionExerciseState? {
        val exercises = _state.value.exercises
        val index = _state.value.currentExerciseIndex
        return if (exercises.isNotEmpty() && index < exercises.size) {
            exercises[index]
        } else null
    }

    fun addSet(reps: Int, weight: Float? = null) {
        val currentExercise = getCurrentExercise() ?: return
        val currentState = _state.value

        viewModelScope.launch {
            try {
                // Получаем текущие подходы
                val existingSets = repository.getSetsBySessionExerciseId(currentExercise.sessionExerciseId)
                val setNumber = existingSets.size + 1

                // Создаем новый подход
                val setEntity = WorkoutSetEntity(
                    sessionExerciseId = currentExercise.sessionExerciseId,
                    setNumber = setNumber,
                    reps = reps,
                    weight = weight ?: 0f,
                    isWarmup = false
                )
                repository.insertWorkoutSet(setEntity)

                // Обновляем состояние
                val updatedSets = existingSets + setEntity
                val updatedExercises = currentState.exercises.map { exercise ->
                    if (exercise.sessionExerciseId == currentExercise.sessionExerciseId) {
                        exercise.copy(sets = updatedSets)
                    } else {
                        exercise
                    }
                }

                _state.value = currentState.copy(
                    exercises = updatedExercises
                )
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка добавления подхода: ${e.message}"
            }
        }
    }

    fun removeSet(setId: String) {
        val currentState = _state.value
        val currentExercise = getCurrentExercise() ?: return

        viewModelScope.launch {
            try {
                // Находим подход
                val setToRemove = currentExercise.sets.find { it.id == setId }
                if (setToRemove == null) {
                    _errorMessage.value = "Подход не найден"
                    return@launch
                }

                // Удаляем подход
                repository.deleteWorkoutSet(setId)

                // Обновляем состояние
                val updatedSets = currentExercise.sets.filter { it.id != setId }
                    .mapIndexed { index, set ->
                        set.copy(setNumber = index + 1)
                    }

                val updatedExercises = currentState.exercises.map { exercise ->
                    if (exercise.sessionExerciseId == currentExercise.sessionExerciseId) {
                        exercise.copy(sets = updatedSets)
                    } else {
                        exercise
                    }
                }

                _state.value = currentState.copy(
                    exercises = updatedExercises
                )
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка удаления подхода: ${e.message}"
            }
        }
    }

    fun nextExercise() {
        val currentState = _state.value
        val currentExercise = getCurrentExercise() ?: return

        // Проверяем, есть ли хотя бы один подход
        if (currentExercise.sets.isEmpty()) {
            _errorMessage.value = "Добавьте хотя бы один подход перед переходом"
            return
        }

        val nextIndex = currentState.currentExerciseIndex + 1
        if (nextIndex < currentState.exercises.size) {
            _state.value = currentState.copy(currentExerciseIndex = nextIndex)
        } else {
            // Все упражнения выполнены
            _errorMessage.value = "Все упражнения выполнены! Завершите тренировку."
        }
    }

    fun previousExercise() {
        val currentState = _state.value
        val prevIndex = currentState.currentExerciseIndex - 1
        if (prevIndex >= 0) {
            _state.value = currentState.copy(currentExerciseIndex = prevIndex)
        }
    }

    fun finishSession(duration: Int?) {
        viewModelScope.launch {
            _isSaving.value = true
            try {
                val currentState = _state.value
                repository.completeSession(
                    sessionId = currentState.sessionId,
                    duration = duration,
                    notes = null,
                    feeling = null
                )

                _state.value = currentState.copy(isCompleted = true)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка завершения тренировки: ${e.message}"
            } finally {
                _isSaving.value = false
            }
        }
    }

    @SuppressLint("EmptySuperCall")
    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

    class Factory(
        private val repository: GymRepository,
        private val userId: String,
        private val templateId: String
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WorkoutSessionViewModel::class.java)) {
                return WorkoutSessionViewModel(repository, userId, templateId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
