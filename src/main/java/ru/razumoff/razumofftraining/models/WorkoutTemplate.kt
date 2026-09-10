package ru.razumoff.razumofftraining.models

data class WorkoutTemplate(
    val id: String,
    val name: String,
    val description: String? = null,
    val exercises: List<TemplateExercise> = emptyList(),
    val workoutType: String = WorkoutType.REGULAR.name,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)