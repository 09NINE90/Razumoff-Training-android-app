package ru.razumoff.razumofftraining.ui.screens.workouts

import ru.razumoff.razumofftraining.models.Exercise

data class CreateWorkoutState(
    val date: Long = System.currentTimeMillis(),
    val selectedExercises: List<CreateWorkoutExercise> = emptyList(),
    val duration: Int? = null,
    val notes: String = "",
    val feeling: Int? = null
)

data class CreateWorkoutExercise(
    val exercise: Exercise,
    val sets: MutableList<CreateWorkoutSet> = mutableListOf(),
    val order: Int = 0,
    val notes: String = ""
) {
    fun addSet() {
        sets.add(CreateWorkoutSet(
            setNumber = sets.size + 1,
            reps = 0,
            weight = 0f,
            isWarmup = false
        ))
    }

    fun removeSet(set: CreateWorkoutSet) {
        sets.remove(set)
        sets.forEachIndexed { index, s -> s.setNumber = index + 1 }
    }
}

data class CreateWorkoutSet(
    var setNumber: Int,
    var reps: Int,
    var weight: Float,
    var isWarmup: Boolean
)