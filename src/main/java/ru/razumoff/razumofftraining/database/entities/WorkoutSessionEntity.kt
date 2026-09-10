package ru.razumoff.razumofftraining.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.razumoff.razumofftraining.models.WorkoutType
import java.util.UUID

@Entity(tableName = "workout_sessions")
data class WorkoutSessionEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val templateId: String,          // Ссылка на шаблон
    val templateName: String,        // Денормализация для быстрого отображения
    val date: Long = System.currentTimeMillis(),
    val duration: Int? = null,       // Длительность в минутах
    val notes: String? = null,
    val feeling: Int? = null,        // Самочувствие 1-10
    val isCompleted: Boolean = true,
    val workoutType: String = WorkoutType.REGULAR.name,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)