package ru.razumoff.razo.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.razumoff.razo.database.entities.WorkoutSessionEntity

@Dao
interface WorkoutSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: WorkoutSessionEntity)

    @Query("SELECT * FROM workout_sessions WHERE id = :sessionId AND userId = :userId")
    suspend fun getSessionById(sessionId: String, userId: String): WorkoutSessionEntity?

    @Query("SELECT * FROM workout_sessions WHERE userId = :userId ORDER BY date DESC")
    suspend fun getSessionsByUser(userId: String): List<WorkoutSessionEntity>

    @Query("SELECT * FROM workout_sessions WHERE userId = :userId ORDER BY date DESC LIMIT 10")
    suspend fun getLastTenSessionsByUser(userId: String): List<WorkoutSessionEntity>

    @Query("SELECT * FROM workout_sessions WHERE userId = :userId ORDER BY date DESC LIMIT 10")
    fun observeLastTenSessionsByUser(userId: String): Flow<List<WorkoutSessionEntity>>

    @Query("SELECT * FROM workout_sessions WHERE userId = :userId ORDER BY date DESC LIMIT 5")
    suspend fun getLastFiveSessionsByUser(userId: String): List<WorkoutSessionEntity>

    @Query("DELETE FROM workout_sessions WHERE id = :sessionId AND userId = :userId")
    suspend fun deleteSession(sessionId: String, userId: String)

    @Query(
        """
            UPDATE workout_sessions
            SET workoutType = :workoutType,
                updatedAt = :updatedAt
            WHERE templateId = :templateId
              AND userId = :userId
        """
    )
    suspend fun updateWorkoutTypeByTemplate(
        templateId: String,
        userId: String,
        workoutType: String,
        updatedAt: Long = System.currentTimeMillis()
    )

    @Query("UPDATE workout_sessions SET isCompleted = 1, duration = :duration, notes = :notes, feeling = :feeling, updatedAt = :updatedAt WHERE id = :sessionId")
    suspend fun completeSession(
        sessionId: String,
        duration: Int?,
        notes: String?,
        feeling: Int?,
        updatedAt: Long
    )

    @Query("UPDATE workout_sessions SET date = :newDate, updatedAt = :updatedAt WHERE id = :sessionId")
    suspend fun updateSessionDate(
        sessionId: String,
        newDate: Long,
        updatedAt: Long = System.currentTimeMillis()
    )

    @Query("""
        UPDATE workout_sessions
        SET templateName = :templateName,
            updatedAt = :updatedAt
        WHERE templateId = :templateId
          AND userId = :userId
    """)
    suspend fun updateTemplateNameByTemplate(
        templateId: String,
        userId: String,
        templateName: String,
        updatedAt: Long = System.currentTimeMillis()
    )

    @Query(
        """
        SELECT COUNT(*)
        FROM workout_sessions
        WHERE userId = :userId
          AND isCompleted = 1
          AND (:workoutType IS NULL OR workoutType = :workoutType)
          AND (:fromDate IS NULL OR date >= :fromDate)
    """
    )
    suspend fun getCompletedSessionsCount(
        userId: String,
        workoutType: String?,
        fromDate: Long?
    ): Int
}