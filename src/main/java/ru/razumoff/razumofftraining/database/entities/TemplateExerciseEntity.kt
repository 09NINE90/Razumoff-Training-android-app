package ru.razumoff.razumofftraining.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "template_exercises")
data class TemplateExerciseEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val templateId: String,                     // Ссылка на шаблон
    val exerciseId: String,                     // Ссылка на ExerciseEntity
    val order: Int = 0,                         // Порядок выполнения
    val defaultSets: Int = 3,                   // Количество подходов по умолчанию
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)