package ru.razumoff.razumofftraining.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.razumoff.razumofftraining.models.MuscleGroup
import ru.razumoff.razumofftraining.models.WorkoutExercise
import ru.razumoff.razumofftraining.models.WorkoutSet

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromMuscleGroupsList(value: List<MuscleGroup>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toMuscleGroupsList(value: String): List<MuscleGroup> {
        val type = object : TypeToken<List<MuscleGroup>>() {}.type
        return gson.fromJson(value, type) ?: emptyList()
    }

    @TypeConverter
    fun fromWorkoutExercisesList(value: List<WorkoutExercise>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toWorkoutExercisesList(value: String): List<WorkoutExercise> {
        val type = object : TypeToken<List<WorkoutExercise>>() {}.type
        return gson.fromJson(value, type) ?: emptyList()
    }

    @TypeConverter
    fun fromWorkoutSetsList(value: List<WorkoutSet>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toWorkoutSetsList(value: String): List<WorkoutSet> {
        val type = object : TypeToken<List<WorkoutSet>>() {}.type
        return gson.fromJson(value, type) ?: emptyList()
    }
}