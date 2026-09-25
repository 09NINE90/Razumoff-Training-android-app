package ru.razumoff.razo.ui.screens.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.razumoff.razo.database.entities.UserEntity
import ru.razumoff.razo.database.entities.UserMeasurementEntity
import ru.razumoff.razo.database.repository.GymRepository
import ru.razumoff.razo.di.AppContainer
import ru.razumoff.razo.models.MeasurementType
import ru.razumoff.razo.models.unit

class UserViewModel(
    private val repository: GymRepository,
    private val appContainer: AppContainer
) : ViewModel() {

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user.asStateFlow()

    private val _measurements =
        MutableStateFlow<Map<MeasurementType, UserMeasurementEntity>>(emptyMap())

    val measurements: StateFlow<Map<MeasurementType, UserMeasurementEntity>> =
        _measurements.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadOrCreateUser()
    }

    fun saveProfile(
        name: String,
        birthDate: Long?,
        measurements: Map<MeasurementType, String>
    ) {
        viewModelScope.launch {
            try {
                val currentUser = _user.value ?: return@launch
                val now = System.currentTimeMillis()

                // Сохраняем основные данные пользователя
                val updatedUser = currentUser.copy(
                    name = name,
                    birthDate = birthDate,
                    updatedAt = now
                )

                repository.insertOrUpdateUser(updatedUser)
                _user.value = updatedUser

                // Сохраняем только новые или изменённые замеры.
                // Старые записи не изменяем и не удаляем.
                measurements.forEach { (type, value) ->
                    val newValue = value
                        .replace(',', '.')
                        .toFloatOrNull()
                        ?: return@forEach

                    val oldValue = _measurements.value[type]?.value

                    if (oldValue == null || oldValue != newValue) {
                        repository.insertMeasurement(
                            UserMeasurementEntity(
                                userId = currentUser.id,
                                value = newValue,
                                type = type.name,
                                unit = type.unit().name,
                                dateTime = now
                            )
                        )
                    }
                }

                // Перечитываем последние значения для UI
                loadMeasurements(currentUser.id)

                _errorMessage.value = null

            } catch (e: Exception) {
                _errorMessage.value = "Ошибка сохранения профиля: ${e.message}"
            }
        }
    }

    fun loadOrCreateUser() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                var existingUser = repository.getCurrentUser()

                if (existingUser == null) {
                    val newUser = UserEntity()
                    repository.insertOrUpdateUser(newUser)

                    existingUser = repository.getCurrentUser()

                    existingUser?.let { user ->
                        appContainer.initializeDefaultExercises(user.id)
                    }
                }

                _user.value = existingUser

                existingUser?.let { user ->
                    loadMeasurements(user.id)
                }

                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value =
                    "Ошибка загрузки пользователя: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun loadMeasurements(userId: String) {
        val measurements = repository.getMeasurementsByUser(userId)

        _measurements.value = measurements
            .sortedByDescending { it.dateTime }
            .mapNotNull { measurement ->
                val type = MeasurementType.fromString(measurement.type)
                    ?: return@mapNotNull null

                type to measurement
            }
            .distinctBy { it.first }
            .toMap()
    }

    fun refreshUser() {
        loadOrCreateUser()
    }

    class Factory(
        private val repository: GymRepository,
        private val appContainer: AppContainer
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>
        ): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                return UserViewModel(
                    repository,
                    appContainer
                ) as T
            }

            throw IllegalArgumentException(
                "Unknown ViewModel class"
            )
        }
    }
}