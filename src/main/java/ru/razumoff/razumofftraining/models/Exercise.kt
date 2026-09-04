package ru.razumoff.razumofftraining.models

data class Exercise(
    val id: String,
    val name: String,
    val description: String,
    val muscleGroups: List<MuscleGroup>,
    val bodyPart: BodyPart,
    val movementType: MovementType,
    val equipment: String?,
    val notes: String?,
    val isCustom: Boolean
)