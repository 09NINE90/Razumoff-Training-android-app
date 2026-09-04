package ru.razumoff.razumofftraining.models

data class TemplateExercise(
    val id: String = "",
    val exerciseId: String,
    val exerciseName: String = "",
    val order: Int = 0,
    val notes: String? = null
)