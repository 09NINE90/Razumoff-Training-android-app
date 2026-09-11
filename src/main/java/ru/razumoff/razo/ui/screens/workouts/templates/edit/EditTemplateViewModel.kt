package ru.razumoff.razo.ui.screens.workouts.templates.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.razumoff.razo.database.repository.GymRepository
import ru.razumoff.razo.models.WorkoutType

class EditTemplateViewModel(
    private val repository: GymRepository,
    private val userId: String,
    private val templateId: String
) : ViewModel() {

    private val _templateName = MutableStateFlow("")
    val templateName: StateFlow<String> = _templateName.asStateFlow()

    private val _templateDescription = MutableStateFlow("")
    val templateDescription: StateFlow<String> =
        _templateDescription.asStateFlow()

    private val _workoutType =
        MutableStateFlow(WorkoutType.REGULAR)
    val workoutType: StateFlow<WorkoutType> =
        _workoutType.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadTemplate()
    }

    private fun loadTemplate() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val template =
                    repository.getTemplateById(templateId, userId)

                if (template == null) {
                    _errorMessage.value = "Шаблон не найден"
                    return@launch
                }

                _templateName.value = template.name
                _templateDescription.value = template.description.orEmpty()

                _workoutType.value =
                    runCatching {
                        WorkoutType.valueOf(template.workoutType)
                    }.getOrDefault(WorkoutType.REGULAR)

                _errorMessage.value = null

            } catch (e: Exception) {
                _errorMessage.value =
                    "Ошибка загрузки шаблона: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateTemplateName(value: String) {
        _templateName.value = value
    }

    fun updateTemplateDescription(value: String) {
        _templateDescription.value = value
    }

    fun setWorkoutType(type: WorkoutType) {
        _workoutType.value = type
    }

    fun saveTemplate(onSuccess: () -> Unit) {
        val name = _templateName.value.trim()

        if (name.isEmpty()) {
            _errorMessage.value = "Введите название тренировки"
            return
        }

        viewModelScope.launch {
            _isSaving.value = true

            try {
                // Обновляем основные данные шаблона
                repository.updateTemplate(
                    templateId = templateId,
                    userId = userId,
                    name = name,
                    description = _templateDescription.value
                        .trim()
                        .takeIf { it.isNotEmpty() },
                    workoutType = _workoutType.value
                )

                _errorMessage.value = null

                onSuccess()

            } catch (e: Exception) {
                _errorMessage.value =
                    "Ошибка сохранения: ${e.message}"
            } finally {
                _isSaving.value = false
            }
        }
    }

    fun deleteTemplate(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isSaving.value = true

            try {
                repository.deleteTemplate(
                    templateId = templateId,
                    userId = userId
                )

                _errorMessage.value = null

                onSuccess()

            } catch (e: Exception) {
                _errorMessage.value =
                    "Ошибка удаления шаблона: ${e.message}"
            } finally {
                _isSaving.value = false
            }
        }
    }

    class Factory(
        private val repository: GymRepository,
        private val userId: String,
        private val templateId: String
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>
        ): T {
            if (
                modelClass.isAssignableFrom(
                    EditTemplateViewModel::class.java
                )
            ) {
                return EditTemplateViewModel(
                    repository,
                    userId,
                    templateId
                ) as T
            }

            throw IllegalArgumentException(
                "Unknown ViewModel class"
            )
        }
    }
}
