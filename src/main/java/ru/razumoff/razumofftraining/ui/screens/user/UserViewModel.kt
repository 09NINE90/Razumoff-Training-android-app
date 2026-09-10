package ru.razumoff.razumofftraining.ui.screens.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.razumoff.razumofftraining.database.entities.UserEntity
import ru.razumoff.razumofftraining.database.entities.UserMeasurementEntity
import ru.razumoff.razumofftraining.database.repository.GymRepository
import ru.razumoff.razumofftraining.di.AppContainer
import ru.razumoff.razumofftraining.models.MeasurementType
import ru.razumoff.razumofftraining.models.MeasurementUnit

class UserViewModel(
    private val repository: GymRepository,
    private val appContainer: AppContainer
) : ViewModel() {

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadOrCreateUser()
    }

    fun loadOrCreateUser() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Пытаемся получить существующего пользователя
                var existingUser = repository.getCurrentUser()

                if (existingUser == null) {
                    // Если пользователя нет, создаем нового
                    val newUser = UserEntity()
                    repository.insertOrUpdateUser(newUser)
                    existingUser = repository.getCurrentUser()
                    existingUser?.let { user ->
                        appContainer.initializeDefaultExercises(user.id)
                    }
                }

//                appContainer.initializeDefaultExercises(existingUser?.id.toString())

                _user.value = existingUser
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка загрузки пользователя: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadCurrentUser() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val existingUser = repository.getCurrentUser()
                _user.value = existingUser
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка загрузки пользователя: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateUser(
        name: String,
        birthDate: Long?,
        weight: Float?,
        height: Float?
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val currentUser = _user.value ?: return@launch
                val updatedUser = currentUser.copy(
                    name = name,
                    birthDate = birthDate,
                    weight = weight,
                    height = height,
                    updatedAt = System.currentTimeMillis()
                )
                repository.insertOrUpdateUser(updatedUser)

                if (weight != null && weight > 0f) {
                    val weightChanged = currentUser.weight != weight
                    if (weightChanged) {
                        val measurement = UserMeasurementEntity(
                            userId = currentUser.id,
                            value = weight,
                            type = MeasurementType.WEIGHT.name,
                            unit = MeasurementUnit.KG.name,
                            dateTime = System.currentTimeMillis()
                        )
                        repository.insertMeasurement(measurement)
                    }
                }

                _user.value = updatedUser
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка обновления пользователя: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveMeasurement(
        value: Float,
        type: MeasurementType,
        unit: MeasurementUnit,
        dateTime: Long = System.currentTimeMillis()
    ) {
        viewModelScope.launch {
            try {
                val currentUser = _user.value ?: return@launch
                val measurement = UserMeasurementEntity(
                    userId = currentUser.id,
                    value = value,
                    type = type.name,
                    unit = unit.name,
                    dateTime = dateTime
                )
                repository.insertMeasurement(measurement)

                // Если это вес — обновляем пользователя
                if (type == MeasurementType.WEIGHT) {
                    val updatedUser = currentUser.copy(
                        weight = value,
                        updatedAt = System.currentTimeMillis()
                    )
                    repository.insertOrUpdateUser(updatedUser)
                    _user.value = updatedUser
                }

                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка сохранения замера: ${e.message}"
            }
        }
    }

    fun refreshUser() {
        loadOrCreateUser()
    }

    // Фабрика для создания ViewModel
    class Factory(
        private val repository: GymRepository,
        private val appContainer: AppContainer
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                return UserViewModel(repository, appContainer) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}