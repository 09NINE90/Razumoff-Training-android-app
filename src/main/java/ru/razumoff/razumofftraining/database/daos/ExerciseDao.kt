package ru.razumoff.razumofftraining.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.razumoff.razumofftraining.database.entities.ExerciseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: ExerciseEntity)

    @Query("SELECT * FROM exercises ORDER BY bodyPart, muscleGroups, name ASC")
    suspend fun getAllExercises(): List<ExerciseEntity>

    @Query("SELECT COUNT(*) FROM exercises")
    suspend fun getExercisesCount(): Int

    @Query("SELECT COUNT(*) FROM exercises")
    fun observeExercisesCount(): Flow<Int>

    @Query("DELETE FROM exercises")
    suspend fun deleteAllExercises()

    @Query("SELECT * FROM exercises WHERE userId = :userId ORDER BY name ASC")
    fun getExercisesByUser(userId: String): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises WHERE id = :exerciseId AND userId = :userId")
    suspend fun getExerciseById(exerciseId: String, userId: String): ExerciseEntity?

    @Query("SELECT * FROM exercises WHERE userId = :userId AND isCustom = 1")
    fun getCustomExercises(userId: String): Flow<List<ExerciseEntity>>

    @Query("DELETE FROM exercises WHERE id = :exerciseId AND userId = :userId")
    suspend fun deleteExercise(exerciseId: String, userId: String)

    @Query("SELECT * FROM exercises WHERE userId = :userId ORDER BY name ASC")
    suspend fun getExercisesByUserId(userId: String): List<ExerciseEntity>

    @Query("DELETE FROM exercises WHERE id = :exerciseId AND userId = :userId")
    suspend fun deleteExerciseById(exerciseId: String, userId: String)

    @Delete
    suspend fun deleteExercise(exercise: ExerciseEntity)

    @Query("UPDATE exercises SET name = :name, description = :description, muscleGroups = :muscleGroups, bodyPart = :bodyPart, movementType = :movementType, equipment = :equipment, notes = :notes, updatedAt = :updatedAt WHERE id = :exerciseId AND userId = :userId")
    suspend fun updateExercise(
        exerciseId: String,
        userId: String,
        name: String,
        description: String,
        muscleGroups: String,
        bodyPart: String,
        movementType: String,
        equipment: String?,
        notes: String?,
        updatedAt: Long
    )
}