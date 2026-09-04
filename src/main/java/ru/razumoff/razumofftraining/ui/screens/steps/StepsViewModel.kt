package ru.razumoff.razumofftraining.ui.screens.steps

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.time.TimeRangeFilter
import kotlinx.coroutines.launch
import ru.razumoff.razumofftraining.ui.components.charts.WeeklyStepData
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

class StepsViewModel : ViewModel() {

    private val LOG_TAG = "FIT_DEBUG"

    // Состояния
    private val _isConnected = mutableStateOf(false)
    val isConnected: Boolean get() = _isConnected.value

    private val _stepsCount = mutableStateOf<Long?>(null)
    val stepsCount: Long? get() = _stepsCount.value

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: String? get() = _errorMessage.value

    private val _isLoading = mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading.value

    private val _weeklyData = mutableStateOf<List<WeeklyStepData>>(emptyList())
    val weeklyData: List<WeeklyStepData> get() = _weeklyData.value

    private val _weeklyTotal = mutableStateOf(0)
    val weeklyTotal: Int get() = _weeklyTotal.value

    private val _weeklyAverage = mutableStateOf(0)
    val weeklyAverage: Int get() = _weeklyAverage.value

    private val _dailyGoal = mutableStateOf(5000)
    val dailyGoal: Int get() = _dailyGoal.value

    private val permissions = setOf(
        HealthPermission.getReadPermission(StepsRecord::class)
    )

    // Лаунчер для запроса разрешений (будет передан из Activity)
    private var requestPermissionsLauncher: ((Set<String>) -> Unit)? = null

    fun setRequestPermissionsLauncher(launcher: (Set<String>) -> Unit) {
        requestPermissionsLauncher = launcher
    }

    // Проверка доступности Health Connect
    fun checkHealthConnect(context: Context) {
        try {
            val sdkStatus = HealthConnectClient.getSdkStatus(context)
            Log.d(LOG_TAG, "Статус Health Connect: $sdkStatus (1=Доступен)")

            if (sdkStatus == HealthConnectClient.SDK_AVAILABLE) {
                Log.d(LOG_TAG, "Health Connect доступен")
                checkPermissions(context)
            } else {
                Log.w(LOG_TAG, "Health Connect не доступен")
                _errorMessage.value = "Health Connect не установлен"
                _isConnected.value = false
            }
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Ошибка: ${e.message}", e)
            _errorMessage.value = "Ошибка: ${e.message}"
        }
    }

    // Проверка разрешений
    fun checkPermissions(context: Context) {
        val client = HealthConnectClient.getOrCreate(context)
        viewModelScope.launch {
            try {
                val granted = client.permissionController.getGrantedPermissions()
                Log.d(LOG_TAG, "Выданные разрешения: $granted")

                if (granted.containsAll(permissions)) {
                    Log.d(LOG_TAG, "Все разрешения есть!")
                    _isConnected.value = true
                    _errorMessage.value = null
                    loadStepsLastSevenDays(context)
                } else {
                    Log.w(LOG_TAG, "Нужны разрешения")
                    _isConnected.value = false
                    _errorMessage.value = "Требуются разрешения"
                }
            } catch (e: Exception) {
                Log.e(LOG_TAG, "Ошибка: ${e.message}", e)
                _errorMessage.value = "Ошибка: ${e.message}"
            }
        }
    }

    // Запрос разрешений
    fun checkAndRequestPermissions(context: Context) {
        val client = HealthConnectClient.getOrCreate(context)
        viewModelScope.launch {
            try {
                val granted = client.permissionController.getGrantedPermissions()
                if (granted.containsAll(permissions)) {
                    _isConnected.value = true
                    _errorMessage.value = null
                    loadStepsLastSevenDays(context)
                } else {
                    Log.d(LOG_TAG, "Запрашиваем разрешения...")
                    requestPermissionsLauncher?.invoke(permissions)
                }
            } catch (e: Exception) {
                Log.e(LOG_TAG, "Ошибка: ${e.message}", e)
                _errorMessage.value = "Ошибка: ${e.message}"
            }
        }
    }

    // Загрузка шагов
    fun loadStepsToday(context: Context) {
        Log.d(LOG_TAG, "Загрузка шагов...")
        val client = HealthConnectClient.getOrCreate(context)

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val zoneId = ZoneId.systemDefault()
                val now = LocalDate.now(zoneId)

                val startOfDay = now.atStartOfDay(zoneId).toInstant()
                val endOfDay = now.plusDays(1).atStartOfDay(zoneId).toInstant()

                Log.d(LOG_TAG, "Временная зона: ${zoneId.id}")
                Log.d(LOG_TAG, "Начало дня (локальное): ${now.atStartOfDay(zoneId)}")
                Log.d(LOG_TAG, "Диапазон: $startOfDay - $endOfDay")

                val response = client.aggregate(
                    AggregateRequest(
                        metrics = setOf(StepsRecord.COUNT_TOTAL),
                        timeRangeFilter = TimeRangeFilter.between(startOfDay, endOfDay)
                    )
                )

                Log.d(LOG_TAG, "Ответ: $response")

                val totalSteps = response[StepsRecord.COUNT_TOTAL]
                Log.d(LOG_TAG, "Получено шагов: $totalSteps")

                _stepsCount.value = totalSteps ?: 0L
                _errorMessage.value = null

            } catch (e: Exception) {
                Log.e(LOG_TAG, "Ошибка загрузки шагов: ${e.message}", e)
                _errorMessage.value = "Ошибка загрузки: ${e.message}"
                _stepsCount.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadStepsWeek(context: Context){
        Log.d(LOG_TAG, "Загрузка статистики за неделю...")
        val client = HealthConnectClient.getOrCreate(context)

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val zoneId = ZoneId.systemDefault()
                val today = LocalDate.now(zoneId)

                // Получаем начало недели (понедельник)
                val startOfWeek = today.with(DayOfWeek.MONDAY)
                val endOfWeek = startOfWeek.plusDays(7)

                Log.d(LOG_TAG, "Неделя: $startOfWeek - $endOfWeek")

                // Получаем цель из Health Connect (если есть)
                loadDailyGoal(context)

                // Получаем данные за каждый день недели
                val weeklySteps = mutableListOf<WeeklyStepData>()
                var totalSteps = 0

                for (i in 0 until 7) {
                    val date = startOfWeek.plusDays(i.toLong())
                    val startOfDay = date.atStartOfDay(zoneId).toInstant()
                    val endOfDay = date.plusDays(1).atStartOfDay(zoneId).toInstant()

                    val response = client.aggregate(
                        AggregateRequest(
                            metrics = setOf(StepsRecord.COUNT_TOTAL),
                            timeRangeFilter = TimeRangeFilter.between(startOfDay, endOfDay)
                        )
                    )

                    val steps = response[StepsRecord.COUNT_TOTAL] ?: 0L
                    val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())

                    weeklySteps.add(
                        WeeklyStepData(
                            day = dayName,
                            steps = steps.toInt(),
                            goal = _dailyGoal.value
                        )
                    )
                    totalSteps += steps.toInt()

                    Log.d(LOG_TAG, "${date}: $steps шагов")
                }

                _weeklyData.value = weeklySteps
                _weeklyTotal.value = totalSteps
                _weeklyAverage.value = if (weeklySteps.isNotEmpty()) totalSteps / weeklySteps.size else 0

                // Загружаем данные за сегодня
                loadStepsToday(context)

                _errorMessage.value = null

            } catch (e: Exception) {
                Log.e(LOG_TAG, "Ошибка загрузки недельной статистики: ${e.message}", e)
                _errorMessage.value = "Ошибка загрузки: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadStepsLastSevenDays(context: Context) {
        Log.d(LOG_TAG, "Загрузка статистики за последние 7 дней...")
        val client = HealthConnectClient.getOrCreate(context)

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val zoneId = ZoneId.systemDefault()
                val today = LocalDate.now(zoneId)

                val days = (0 until 7).map { today.minusDays(it.toLong()) }.reversed()

                Log.d(LOG_TAG, "Период: ${days.first()} - ${days.last()} (последние 7 дней)")

                loadDailyGoal(context)

                val weeklySteps = mutableListOf<WeeklyStepData>()
                var totalSteps = 0

                days.forEach { date ->
                    val startOfDay = date.atStartOfDay(zoneId).toInstant()
                    val endOfDay = date.plusDays(1).atStartOfDay(zoneId).toInstant()

                    val response = client.aggregate(
                        AggregateRequest(
                            metrics = setOf(StepsRecord.COUNT_TOTAL),
                            timeRangeFilter = TimeRangeFilter.between(startOfDay, endOfDay)
                        )
                    )

                    val steps = response[StepsRecord.COUNT_TOTAL] ?: 0L

                    val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())

                    val label = if (date == today) {
                        "Сегодня"
                    } else {
                        "$dayName ${date.dayOfMonth}"
                    }

                    weeklySteps.add(
                        WeeklyStepData(
                            day = label,
                            steps = steps.toInt(),
                            goal = _dailyGoal.value
                        )
                    )
                    totalSteps += steps.toInt()

                    Log.d(LOG_TAG, "${date}: $steps шагов")
                }

                _weeklyData.value = weeklySteps
                _weeklyTotal.value = totalSteps
                _weeklyAverage.value = if (weeklySteps.isNotEmpty()) totalSteps / weeklySteps.size else 0

                loadStepsToday(context)

                _errorMessage.value = null

            } catch (e: Exception) {
                Log.e(LOG_TAG, "Ошибка загрузки статистики: ${e.message}", e)
                _errorMessage.value = "Ошибка загрузки: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadDailyGoal(context: Context) {
        try {
            val client = HealthConnectClient.getOrCreate(context)
            viewModelScope.launch {
                try {
                    _dailyGoal.value = 10_000 // Дефолтная цель
                    Log.d(LOG_TAG, "Цель на день: ${_dailyGoal.value}")
                } catch (e: Exception) {
                    Log.e(LOG_TAG, "Ошибка загрузки цели: ${e.message}")
                    _dailyGoal.value = 10_000
                }
            }
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Ошибка загрузки цели: ${e.message}")
            _dailyGoal.value = 10_000
        }
    }

    // Обновить все данные
    fun refreshAllData(context: Context) {
        loadStepsLastSevenDays(context)
    }


    // Обработка результата запроса разрешений
    fun onPermissionsResult(granted: Set<String>, context: Context) {
        Log.d(LOG_TAG, "Результат запроса разрешений: $granted")
        if (granted.containsAll(permissions)) {
            Log.d(LOG_TAG, "Все разрешения получены!")
            _isConnected.value = true
            _errorMessage.value = null
            loadStepsLastSevenDays(context)
        } else {
            Log.w(LOG_TAG, "Не все разрешения получены")
            _isConnected.value = false
            _errorMessage.value = "Разрешения не получены"
        }
    }
}