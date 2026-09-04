package ru.razumoff.razumofftraining.models

data class WorkoutSession(
    val id: String,
    val templateId: String,
    val templateName: String,
    val date: Long,
    val exercises: List<SessionExercise>,
    val duration: Int? = null,
    val notes: String? = null,
    val feeling: Int? = null,
    val isCompleted: Boolean = false
)

data class SessionExercise(
    val id: String,
    val sessionId: String,
    val templateExerciseId: String,
    val exerciseId: String,
    val exerciseName: String,
    val order: Int,
    val sets: List<WorkoutSet>,
    val notes: String? = null
)

data class WorkoutSet(
    val id: String,
    val sessionExerciseId: String,
    val setNumber: Int,
    val reps: Int,
    val weight: Float,
    val isWarmup: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)