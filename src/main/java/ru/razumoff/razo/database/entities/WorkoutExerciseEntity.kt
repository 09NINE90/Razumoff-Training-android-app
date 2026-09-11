package ru.razumoff.razo.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "workout_exercises")
data class WorkoutExerciseEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val workoutId: String,              // Ссылка на тренировку
    val exerciseId: String,             // Ссылка на упражнение (из таблицы exercises)
    val order: Int = 0,                 // Порядок выполнения в тренировке
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)