package ru.razumoff.razumofftraining.models

data class Workout(
    val id: String,
    val date: Long,
    val exercises: List<WorkoutExercise>,
    val duration: Int? = null,
    val notes: String? = null,
    val feeling: Int? = null
)