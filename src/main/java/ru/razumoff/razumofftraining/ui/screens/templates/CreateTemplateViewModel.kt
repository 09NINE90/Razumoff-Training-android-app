package ru.razumoff.razumofftraining.ui.screens.templates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.razumoff.razumofftraining.database.entities.TemplateExerciseEntity
import ru.razumoff.razumofftraining.database.entities.WorkoutTemplateEntity
import ru.razumoff.razumofftraining.database.mappers.ExerciseMapper
import ru.razumoff.razumofftraining.models.Exercise
import ru.razumoff.razumofftraining.models.TemplateExercise
import ru.razumoff.razumofftraining.database.repository.GymRepository
import java.util.UUID

class CreateTemplateViewModel(
    private val repository: GymRepository,
    private val userId: String
) : ViewModel() {

    // Состояние
    private val _templateName = MutableStateFlow("")
    val templateName: StateFlow<String> = _templateName.asStateFlow()

    private val _templateDescription = MutableStateFlow("")
    val templateDescription: StateFlow<String> = _templateDescription.asStateFlow()

    private val _selectedExercises = MutableStateFlow<List<TemplateExercise>>(emptyList())
    val selectedExercises: StateFlow<List<TemplateExercise>> = _selectedExercises.asStateFlow()

    private val _availableExercises = MutableStateFlow<List<Exercise>>(emptyList())
    val availableExercises: StateFlow<List<Exercise>> = _availableExercises.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadExercises()
    }

    fun loadExercises() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val entities = repository.getExercisesByUserId(userId)
                val exercises = entities.map { ExerciseMapper.toDomain(it) }
                _availableExercises.value = exercises
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка загрузки упражнений: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateTemplateName(name: String) {
        _templateName.value = name
    }

    fun updateTemplateDescription(description: String) {
        _templateDescription.value = description
    }

    fun addExercise(exercise: Exercise) {
        if (_selectedExercises.value.any { it.exerciseId == exercise.id }) {
            _errorMessage.value = "Упражнение уже добавлено"
            return
        }

        val newExercise = TemplateExercise(
            exerciseId = exercise.id,
            exerciseName = exercise.name,
            order = _selectedExercises.value.size + 1
        )

        _selectedExercises.value = _selectedExercises.value + newExercise
        _errorMessage.value = null
    }

    fun removeExercise(exerciseId: String) {
        _selectedExercises.value = _selectedExercises.value
            .filter { it.exerciseId != exerciseId }
            .mapIndexed { index, exercise -> exercise.copy(order = index + 1) }
    }

    fun moveExercise(fromIndex: Int, toIndex: Int) {
        val list = _selectedExercises.value.toMutableList()
        val item = list.removeAt(fromIndex)
        list.add(toIndex, item)
        _selectedExercises.value = list.mapIndexed { index, exercise ->
            exercise.copy(order = index + 1)
        }
    }

    fun saveTemplate(): Boolean {
        val name = _templateName.value.trim()
        if (name.isEmpty()) {
            _errorMessage.value = "Введите название тренировки"
            return false
        }

        if (_selectedExercises.value.isEmpty()) {
            _errorMessage.value = "Добавьте хотя бы одно упражнение"
            return false
        }

        viewModelScope.launch {
            _isSaving.value = true
            try {
                val templateId = UUID.randomUUID().toString()

                // 1. Сохраняем шаблон
                val templateEntity = WorkoutTemplateEntity(
                    id = templateId,
                    userId = userId,
                    name = name,
                    description = _templateDescription.value.takeIf { it.isNotBlank() }
                )
                repository.insertTemplate(templateEntity)

                // 2. Сохраняем упражнения в шаблоне
                val exerciseEntities = _selectedExercises.value.mapIndexed { index, exercise ->
                    TemplateExerciseEntity(
                        templateId = templateId,
                        exerciseId = exercise.exerciseId,
                        order = index + 1,
                        notes = exercise.notes
                    )
                }
                repository.insertTemplateExercises(exerciseEntities)

                _errorMessage.value = null
                _isSaving.value = false
                return@launch
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка сохранения: ${e.message}"
                _isSaving.value = false
            }
        }
        return true
    }

    fun resetState() {
        _templateName.value = ""
        _templateDescription.value = ""
        _selectedExercises.value = emptyList()
        _errorMessage.value = null
    }

    class Factory(
        private val repository: GymRepository,
        private val userId: String
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CreateTemplateViewModel::class.java)) {
                return CreateTemplateViewModel(repository, userId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}