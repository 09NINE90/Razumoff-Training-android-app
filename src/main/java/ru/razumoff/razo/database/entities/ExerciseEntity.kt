package ru.razumoff.razo.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val name: String,
    val description: String = "",
    val muscleGroups: String, // Сохраняем как JSON строку
    val bodyPart: String,
    val movementType: String,
    val equipment: String? = null,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isCustom: Boolean = true
)