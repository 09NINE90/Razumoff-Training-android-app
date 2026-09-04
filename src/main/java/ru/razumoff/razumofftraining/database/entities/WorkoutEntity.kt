package ru.razumoff.razumofftraining.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val date: Long = System.currentTimeMillis(),
    val duration: Int? = null,          // Длительность в минутах
    val notes: String? = null,
    val feeling: Int? = null,           // Самочувствие 1-10
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)