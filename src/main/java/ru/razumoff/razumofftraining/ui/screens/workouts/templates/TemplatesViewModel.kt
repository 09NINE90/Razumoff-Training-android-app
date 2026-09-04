package ru.razumoff.razumofftraining.ui.screens.workouts.templates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.razumoff.razumofftraining.database.mappers.WorkoutTemplateMapper
import ru.razumoff.razumofftraining.models.TemplateExercise
import ru.razumoff.razumofftraining.models.WorkoutTemplate
import ru.razumoff.razumofftraining.database.repository.GymRepository

class TemplatesViewModel(
    private val repository: GymRepository,
    private val userId: String
) : ViewModel() {

    private val _templates = MutableStateFlow<List<WorkoutTemplate>>(emptyList())
    val templates: StateFlow<List<WorkoutTemplate>> = _templates.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadTemplates()
    }

    fun loadTemplates() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Получаем все шаблоны
                val templateEntities = repository.getTemplatesByUser(userId)

                // Для каждого шаблона получаем упражнения
                val templates = templateEntities.map { templateEntity ->
                    val exerciseEntities = repository.getTemplateExercises(templateEntity.id)
                    val exercises = exerciseEntities.map { exerciseEntity ->
                        // Получаем название упражнения из ExerciseEntity
                        val exercise = repository.getExerciseById(exerciseEntity.exerciseId, userId)
                        TemplateExercise(
                            id = exerciseEntity.id,
                            exerciseId = exerciseEntity.exerciseId,
                            exerciseName = exercise?.name ?: "Неизвестное упражнение",
                            order = exerciseEntity.order,
                            notes = exerciseEntity.notes
                        )
                    }

                    WorkoutTemplateMapper.toDomain(templateEntity, exercises)
                }

                _templates.value = templates
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка загрузки шаблонов: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteTemplate(templateId: String) {
        viewModelScope.launch {
            try {
                repository.deleteTemplate(templateId, userId)
                loadTemplates()
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка удаления шаблона: ${e.message}"
            }
        }
    }

    fun getExercisesCount(templateId: String): Int {
        val template = _templates.value.find { it.id == templateId }
        return template?.exercises?.size ?: 0
    }

    class Factory(
        private val repository: GymRepository,
        private val userId: String
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TemplatesViewModel::class.java)) {
                return TemplatesViewModel(repository, userId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}