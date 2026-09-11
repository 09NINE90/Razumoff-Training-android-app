package ru.razumoff.razo.database.mappers

import ru.razumoff.razo.database.entities.WorkoutTemplateEntity
import ru.razumoff.razo.models.WorkoutTemplate
import ru.razumoff.razo.models.TemplateExercise

object WorkoutTemplateMapper {

    fun toDomain(
        templateEntity: WorkoutTemplateEntity,
        exercises: List<TemplateExercise>
    ): WorkoutTemplate {
        return WorkoutTemplate(
            id = templateEntity.id,
            name = templateEntity.name,
            description = templateEntity.description,
            exercises = exercises,
            createdAt = templateEntity.createdAt,
            updatedAt = templateEntity.updatedAt
        )
    }

    fun toEntity(domain: WorkoutTemplate, userId: String): WorkoutTemplateEntity {
        return WorkoutTemplateEntity(
            id = domain.id,
            userId = userId,
            name = domain.name,
            description = domain.description,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt
        )
    }
}