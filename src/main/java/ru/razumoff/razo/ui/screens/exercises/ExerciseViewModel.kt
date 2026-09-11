package ru.razumoff.razo.ui.screens.exercises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.razumoff.razo.database.mappers.ExerciseMapper
import ru.razumoff.razo.models.Exercise
import ru.razumoff.razo.database.repository.GymRepository

class ExerciseViewModel(
    private val repository: GymRepository,
    private val userId: String
) : ViewModel() {

    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises: StateFlow<List<Exercise>> = _exercises.asStateFlow()

    val exercisesCount: StateFlow<Int> =
        repository
            .observeExercisesCount()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0
            )


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    init {
        loadExercises()
    }

    fun loadExercises() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val entities = repository.getAllExercises()
                val domainExercises = entities.map { ExerciseMapper.toDomain(it) }
                _exercises.value = domainExercises
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка загрузки упражнений: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addExercise(
        exercise: Exercise,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isSaving.value = true
            _errorMessage.value = null

            try {
                val entity = ExerciseMapper.toEntity(
                    exercise,
                    userId
                )

                repository.insertExercise(entity)

                loadExercises()

                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value =
                    "Ошибка добавления упражнения: ${e.message}"
            } finally {
                _isSaving.value = false
            }
        }
    }

    class Factory(
        private val repository: GymRepository,
        private val userId: String
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ExerciseViewModel::class.java)) {
                return ExerciseViewModel(repository, userId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
