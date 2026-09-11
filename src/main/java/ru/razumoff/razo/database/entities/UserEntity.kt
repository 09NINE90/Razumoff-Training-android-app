package ru.razumoff.razo.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String = "Аноним",
    val birthDate: Long? = null,
    val weight: Float? = null,
    val height: Float? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)