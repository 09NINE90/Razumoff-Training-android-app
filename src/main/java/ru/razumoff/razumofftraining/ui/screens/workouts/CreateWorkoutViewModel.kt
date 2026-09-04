//package ru.razumoff.razumofftraining.ui.screens.workouts
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import ru.razumoff.razumofftraining.database.entities.WorkoutEntity
//import ru.razumoff.razumofftraining.database.entities.WorkoutExerciseEntity
//import ru.razumoff.razumofftraining.database.entities.WorkoutSetEntity
//import ru.razumoff.razumofftraining.database.mappers.ExerciseMapper
//import ru.razumoff.razumofftraining.models.Exercise
//import ru.razumoff.razumofftraining.repository.GymRepository
//import java.util.UUID
//
//class CreateWorkoutViewModel(
//    private val repository: GymRepository,
//    private val userId: String
//) : ViewModel() {
//
//    private val _state = MutableStateFlow(CreateWorkoutState())
//    val state: StateFlow<CreateWorkoutState> = _state.asStateFlow()
//
//    private val _availableExercises = MutableStateFlow<List<Exercise>>(emptyList())
//    val availableExercises: StateFlow<List<Exercise>> = _availableExercises.asStateFlow()
//
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
//
//    private val _errorMessage = MutableStateFlow<String?>(null)
//    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
//
//    private val _isSaving = MutableStateFlow(false)
//    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()
//
//    private val _saveSuccess = MutableStateFlow(false)
//    val saveSuccess: StateFlow<Boolean> = _saveSuccess.asStateFlow()
//
//    init {
//        loadAvailableExercises()
//    }
//
//    fun loadAvailableExercises() {
//        viewModelScope.launch {
//            _isLoading.value = true
//            try {
//                val entities = repository.getExercisesByUserId(userId)
//                val exercises = entities.map { ExerciseMapper.toDomain(it) }
//                _availableExercises.value = exercises
//                _errorMessage.value = null
//            } catch (e: Exception) {
//                _errorMessage.value = "Ошибка загрузки упражнений: ${e.message}"
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//
//    fun addExercise(exercise: Exercise) {
//        val currentState = _state.value
//        if (currentState.selectedExercises.any { it.exercise.id == exercise.id }) {
//            _errorMessage.value = "Упражнение уже добавлено"
//            return
//        }
//
//        val newExercise = CreateWorkoutExercise(
//            exercise = exercise,
//            order = currentState.selectedExercises.size + 1
//        )
//        // Добавляем первый подход по умолчанию
//        newExercise.addSet()
//
//        _state.value = currentState.copy(
//            selectedExercises = currentState.selectedExercises + newExercise
//        )
//        _errorMessage.value = null
//    }
//
//    fun removeExercise(exerciseId: String) {
//        val currentState = _state.value
//        val updatedExercises = currentState.selectedExercises
//            .filter { it.exercise.id != exerciseId }
//            .mapIndexed { index, exercise ->
//                exercise.copy(order = index + 1)
//            }
//        _state.value = currentState.copy(selectedExercises = updatedExercises)
//    }
//
//    fun addSet(exerciseId: String) {
//        val currentState = _state.value
//        val updatedExercises = currentState.selectedExercises.map { exercise ->
//            if (exercise.exercise.id == exerciseId) {
//                exercise.addSet()
//                exercise
//            } else {
//                exercise
//            }
//        }
//        _state.value = currentState.copy(selectedExercises = updatedExercises)
//    }
//
//    fun removeSet(exerciseId: String, set: CreateWorkoutSet) {
//        val currentState = _state.value
//        val updatedExercises = currentState.selectedExercises.map { exercise ->
//            if (exercise.exercise.id == exerciseId) {
//                exercise.removeSet(set)
//                exercise
//            } else {
//                exercise
//            }
//        }
//        _state.value = currentState.copy(selectedExercises = updatedExercises)
//    }
//
//    fun updateSet(
//        exerciseId: String,
//        set: CreateWorkoutSet,
//        reps: Int? = null,
//        weight: Float? = null,
//        isWarmup: Boolean? = null
//    ) {
//        val currentState = _state.value
//        val updatedExercises = currentState.selectedExercises.map { exercise ->
//            if (exercise.exercise.id == exerciseId) {
//                val updatedSets = exercise.sets.map { s ->
//                    if (s == set) {
//                        s.reps = reps ?: s.reps
//                        s.weight = weight ?: s.weight
//                        s.isWarmup = isWarmup ?: s.isWarmup
//                        s
//                    } else {
//                        s
//                    }
//                }
//                exercise.copy(sets = updatedSets.toMutableList())
//            } else {
//                exercise
//            }
//        }
//        _state.value = currentState.copy(selectedExercises = updatedExercises)
//    }
//
//    fun updateDuration(duration: Int?) {
//        _state.value = _state.value.copy(duration = duration)
//    }
//
//    fun updateNotes(notes: String) {
//        _state.value = _state.value.copy(notes = notes)
//    }
//
//    fun updateFeeling(feeling: Int?) {
//        _state.value = _state.value.copy(feeling = feeling)
//    }
//
//    fun saveWorkout() {
//        viewModelScope.launch {
//            _isSaving.value = true
//            _saveSuccess.value = false
//            try {
//                val currentState = _state.value
//                val workoutId = UUID.randomUUID().toString()
//
//                // 1. Сохраняем тренировку
//                val workoutEntity = WorkoutEntity(
//                    id = workoutId,
//                    userId = userId,
//                    date = currentState.date,
//                    duration = currentState.duration,
//                    notes = currentState.notes.takeIf { it.isNotBlank() },
//                    feeling = currentState.feeling
//                )
//                repository.insertWorkout(workoutEntity)
//
//                // 2. Сохраняем упражнения в тренировке
//                currentState.selectedExercises.forEachIndexed { index, exercise ->
//                    val workoutExerciseId = UUID.randomUUID().toString()
//                    val workoutExerciseEntity = WorkoutExerciseEntity(
//                        id = workoutExerciseId,
//                        workoutId = workoutId,
//                        exerciseId = exercise.exercise.id,
//                        order = index + 1,
//                        notes = exercise.notes.takeIf { it.isNotBlank() }
//                    )
//                    repository.insertWorkoutExercise(workoutExerciseEntity)
//
//                    // 3. Сохраняем подходы
//                    exercise.sets.forEach { set ->
//                        val setEntity = WorkoutSetEntity(
//                            workoutExerciseId = workoutExerciseId,
//                            setNumber = set.setNumber,
//                            reps = set.reps,
//                            weight = set.weight,
//                            isWarmup = set.isWarmup
//                        )
//                        repository.insertWorkoutSet(setEntity)
//                    }
//                }
//
//                _saveSuccess.value = true
//                _errorMessage.value = null
//            } catch (e: Exception) {
//                _errorMessage.value = "Ошибка сохранения тренировки: ${e.message}"
//                _saveSuccess.value = false
//            } finally {
//                _isSaving.value = false
//            }
//        }
//    }
//
//    fun resetState() {
//        _state.value = CreateWorkoutState()
//        _saveSuccess.value = false
//        _errorMessage.value = null
//    }
//
//    class Factory(
//        private val repository: GymRepository,
//        private val userId: String
//    ) : ViewModelProvider.Factory {
//        @Suppress("UNCHECKED_CAST")
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(CreateWorkoutViewModel::class.java)) {
//                return CreateWorkoutViewModel(repository, userId) as T
//            }
//            throw IllegalArgumentException("Unknown ViewModel class")
//        }
//    }
//}