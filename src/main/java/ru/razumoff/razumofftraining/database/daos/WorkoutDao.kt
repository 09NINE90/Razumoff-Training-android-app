package ru.razumoff.razumofftraining.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.razumoff.razumofftraining.database.entities.WorkoutEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutEntity)

    @Query("SELECT * FROM workouts WHERE userId = :userId ORDER BY date DESC")
    fun getWorkoutsByUser(userId: String): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workouts WHERE userId = :userId ORDER BY date DESC")
    suspend fun getWorkoutsByUserSync(userId: String): List<WorkoutEntity>

    @Query("SELECT * FROM workouts WHERE id = :workoutId AND userId = :userId")
    suspend fun getWorkoutById(workoutId: String, userId: String): WorkoutEntity?

    @Query("DELETE FROM workouts WHERE id = :workoutId AND userId = :userId")
    suspend fun deleteWorkout(workoutId: String, userId: String)

    @Query("UPDATE workouts SET duration = :duration, notes = :notes, feeling = :feeling, updatedAt = :updatedAt WHERE id = :workoutId")
    suspend fun updateWorkout(
        workoutId: String,
        duration: Int?,
        notes: String?,
        feeling: Int?,
        updatedAt: Long
    )
}