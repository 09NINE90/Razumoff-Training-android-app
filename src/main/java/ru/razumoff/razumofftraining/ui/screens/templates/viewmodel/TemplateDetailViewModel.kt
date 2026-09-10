package ru.razumoff.razumofftraining.ui.screens.templates.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.razumoff.razumofftraining.models.TemplateExercise
import ru.razumoff.razumofftraining.models.WorkoutTemplate
import ru.razumoff.razumofftraining.database.repository.GymRepository

class TemplateDetailViewModel(
    private val repository: GymRepository,
    private val userId: String,
    private val templateId: String
) : ViewModel() {

    private val _template = MutableStateFlow<WorkoutTemplate?>(null)
    val template: StateFlow<WorkoutTemplate?> = _template.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        observeTemplate()
    }

    private fun observeTemplate() {
        viewModelScope.launch {
            repository
                .observeTemplateById(templateId, userId)
                .collect { templateEntity ->

                    if (templateEntity == null) {
                        _template.value = null
                        _errorMessage.value = "Шаблон не найден"
                        return@collect
                    }

                    try {
                        val exerciseEntities =
                            repository.getTemplateExercises(templateId)

                        val exercises = exerciseEntities.map { entity ->
                            val exercise =
                                repository.getExerciseById(
                                    entity.exerciseId,
                                    userId
                                )

                            TemplateExercise(
                                id = entity.id,
                                exerciseId = entity.exerciseId,
                                exerciseName =
                                    exercise?.name
                                        ?: "Неизвестное упражнение",
                                order = entity.order,
                                notes = entity.notes
                            )
                        }

                        _template.value = WorkoutTemplate(
                            id = templateEntity.id,
                            name = templateEntity.name,
                            description = templateEntity.description,
                            workoutType = templateEntity.workoutType,
                            exercises = exercises,
                            createdAt = templateEntity.createdAt,
                            updatedAt = templateEntity.updatedAt
                        )

                        _errorMessage.value = null

                    } catch (e: Exception) {
                        _errorMessage.value =
                            "Ошибка загрузки шаблона: ${e.message}"
                    }
                }
        }
    }

    class Factory(
        private val repository: GymRepository,
        private val userId: String,
        private val templateId: String
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TemplateDetailViewModel::class.java)) {
                return TemplateDetailViewModel(repository, userId, templateId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}