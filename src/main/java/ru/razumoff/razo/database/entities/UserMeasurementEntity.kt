package ru.razumoff.razo.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "user_measurements")
data class UserMeasurementEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val value: Float,                          // Значение замера
    val type: String,                          // MeasurementType.name
    val unit: String,                          // Единица измерения: "kg", "cm"
    val dateTime: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)