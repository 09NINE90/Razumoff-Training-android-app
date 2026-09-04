package ru.razumoff.razumofftraining.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.razumoff.razumofftraining.database.mappers.ExerciseMapper
import ru.razumoff.razumofftraining.models.Exercise
import ru.razumoff.razumofftraining.database.repository.GymRepository

class ExerciseViewModel(
    private val repository: GymRepository,
    private val userId: String
) : ViewModel() {

    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises: StateFlow<List<Exercise>> = _exercises.asStateFlow()

    private val _exercisesCount = MutableStateFlow(0)
    val exercisesCount: StateFlow<Int> = _exercisesCount.asStateFlow()


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess.asStateFlow()

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

    fun loadExercisesCount() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val entitiesCount = repository.getExercisesCount()
                _exercisesCount.value = entitiesCount
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка загрузки упражнений: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addExercise(exercise: Exercise) {
        viewModelScope.launch {
            _isSaving.value = true
            _saveSuccess.value = false
            try {
                val entity = ExerciseMapper.toEntity(exercise, userId)
                repository.insertExercise(entity)
                loadExercises()
                _errorMessage.value = null
                _saveSuccess.value = true
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка добавления упражнения: ${e.message}"
            }
        }
    }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            try {
                val entity = ExerciseMapper.toEntity(exercise, userId)
                repository.deleteExercise(entity)
                loadExercises()
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка удаления упражнения: ${e.message}"
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