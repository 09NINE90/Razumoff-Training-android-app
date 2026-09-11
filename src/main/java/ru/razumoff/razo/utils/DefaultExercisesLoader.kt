package ru.razumoff.razo.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.razumoff.razo.database.entities.ExerciseEntity
import ru.razumoff.razo.models.MuscleGroup
import java.util.UUID

data class DefaultExerciseJson(
    val name: String,
    val description: String,
    val muscleGroups: List<String>,
    val bodyPart: String,
    val movementType: String,
    val equipment: String?,
    val isCustom: Boolean = false
)

object DefaultExercisesLoader {

    private val gson = Gson()

    fun loadDefaultExercises(context: Context, userId: String): List<ExerciseEntity> {
        return try {
            val json = context.assets.open("default_exercises.json")
                .bufferedReader()
                .use { it.readText() }

            val type = object : TypeToken<List<DefaultExerciseJson>>() {}.type
            val exercises: List<DefaultExerciseJson> = gson.fromJson(json, type)

            exercises.map { jsonExercise ->
                ExerciseEntity(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    name = jsonExercise.name,
                    description = jsonExercise.description,
                    muscleGroups = gson.toJson(
                        jsonExercise.muscleGroups.map { MuscleGroup.valueOf(it) }
                    ),
                    bodyPart = jsonExercise.bodyPart,
                    movementType = jsonExercise.movementType,
                    equipment = jsonExercise.equipment,
                    notes = null,
                    isCustom = jsonExercise.isCustom
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}