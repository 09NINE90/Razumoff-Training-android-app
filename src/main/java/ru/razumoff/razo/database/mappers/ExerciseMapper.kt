package ru.razumoff.razo.database.mappers

import com.google.gson.Gson
import ru.razumoff.razo.database.entities.ExerciseEntity
import ru.razumoff.razo.models.BodyPart
import ru.razumoff.razo.models.Exercise
import ru.razumoff.razo.models.MovementType
import ru.razumoff.razo.models.MuscleGroup

object ExerciseMapper {
    private val gson = Gson()

    fun toDomain(entity: ExerciseEntity): Exercise {
        val muscleGroups = try {
            gson.fromJson(entity.muscleGroups, Array<MuscleGroup>::class.java).toList()
        } catch (e: Exception) {
            emptyList()
        }

        return Exercise(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            muscleGroups = muscleGroups,
            bodyPart = try { BodyPart.valueOf(entity.bodyPart) } catch (e: Exception) { BodyPart.UPPER },
            movementType = try { MovementType.valueOf(entity.movementType) } catch (e: Exception) { MovementType.COMPOUND },
            equipment = entity.equipment,
            notes = entity.notes,
            isCustom = entity.isCustom
        )
    }

    fun toEntity(domain: Exercise, userId: String): ExerciseEntity {
        return ExerciseEntity(
            id = domain.id,
            userId = userId,
            name = domain.name,
            description = domain.description,
            muscleGroups = gson.toJson(domain.muscleGroups),
            bodyPart = domain.bodyPart.name,
            movementType = domain.movementType.name,
            equipment = domain.equipment,
            notes = domain.notes,
            isCustom = domain.isCustom
        )
    }
}