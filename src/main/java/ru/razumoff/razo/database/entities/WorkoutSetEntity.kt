package ru.razumoff.razo.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "workout_sets")
data class WorkoutSetEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val sessionExerciseId: String,      // Ссылка на упражнение в тренировке
    val setNumber: Int = 1,             // Номер подхода (1, 2, 3...)
    val reps: Int,                      // Количество повторений
    val weight: Float = 0f,             // Вес в кг
    val isWarmup: Boolean = false,      // Разминочный подход
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)