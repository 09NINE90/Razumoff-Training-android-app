package ru.razumoff.razumofftraining.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.razumoff.razumofftraining.models.WorkoutType
import java.util.UUID

@Entity(tableName = "workout_templates")
data class WorkoutTemplateEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val name: String,
    val description: String? = null,
    val workoutType: String = WorkoutType.REGULAR.name,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)