package ru.razumoff.razo.models

import java.util.UUID

data class User(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "Аноним",
    val age: Int? = null,
    val weight: Float? = null,
    val height: Float? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)