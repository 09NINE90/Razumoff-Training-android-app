package ru.razumoff.razumofftraining.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.razumoff.razumofftraining.database.entities.WorkoutSetEntity
import ru.razumoff.razumofftraining.models.ExerciseProgressRow
import ru.razumoff.razumofftraining.models.ExerciseStatisticsRow

@Dao
interface WorkoutSetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutSet(workoutSet: WorkoutSetEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutSets(workoutSets: List<WorkoutSetEntity>)

    @Query("SELECT * FROM workout_sets WHERE sessionExerciseId = :sessionExerciseId ORDER BY setNumber ASC")
    suspend fun getSetsBySessionExerciseId(sessionExerciseId: String): List<WorkoutSetEntity>

    @Query("DELETE FROM workout_sets WHERE sessionExerciseId = :sessionExerciseId")
    suspend fun deleteSetsBySessionExerciseId(sessionExerciseId: String)

    @Query("DELETE FROM workout_sets WHERE id = :setId")
    suspend fun deleteWorkoutSet(setId: String)

    @Query(
        """
    SELECT COUNT(*)
    FROM workout_sets ws
    INNER JOIN session_exercises se
        ON ws.sessionExerciseId = se.id
    INNER JOIN workout_sessions s
        ON se.sessionId = s.id
    WHERE s.userId = :userId
      AND s.isCompleted = 1
      AND ws.isWarmup = 0
      AND (:workoutType IS NULL OR s.workoutType = :workoutType)
      AND (:fromDate IS NULL OR s.date >= :fromDate)
    """
    )
    suspend fun getTotalSets(
        userId: String,
        workoutType: String?,
        fromDate: Long?
    ): Int

    @Query(
        """
    SELECT COALESCE(SUM(ws.reps), 0)
    FROM workout_sets ws
    INNER JOIN session_exercises se
        ON ws.sessionExerciseId = se.id
    INNER JOIN workout_sessions s
        ON se.sessionId = s.id
    WHERE s.userId = :userId
      AND s.isCompleted = 1
      AND ws.isWarmup = 0
      AND (:workoutType IS NULL OR s.workoutType = :workoutType)
      AND (:fromDate IS NULL OR s.date >= :fromDate)
    """
    )
    suspend fun getTotalReps(
        userId: String,
        workoutType: String?,
        fromDate: Long?
    ): Int

    @Query(
        """
    SELECT COALESCE(
        SUM(
            CASE
                WHEN ws.weight > 0
                THEN ws.weight * ws.reps
                ELSE 0
            END
        ),
        0
    )
    FROM workout_sets ws
    INNER JOIN session_exercises se
        ON ws.sessionExerciseId = se.id
    INNER JOIN workout_sessions s
        ON se.sessionId = s.id
    WHERE s.userId = :userId
      AND s.isCompleted = 1
      AND ws.isWarmup = 0
      AND (:workoutType IS NULL OR s.workoutType = :workoutType)
      AND (:fromDate IS NULL OR s.date >= :fromDate)
    """
    )
    suspend fun getTotalVolume(
        userId: String,
        workoutType: String?,
        fromDate: Long?
    ): Double

    @Query(
        """
    SELECT
        se.exerciseId AS exerciseId,
        se.exerciseName AS exerciseName,

        COALESCE(
            MAX(
                CASE
                    WHEN ws.isWarmup = 0
                    THEN ws.weight
                    ELSE 0
                END
            ),
            0
        ) AS maxWeight,

        COALESCE(
            SUM(
                CASE
                    WHEN ws.isWarmup = 0 AND ws.weight > 0
                    THEN ws.weight * ws.reps
                    ELSE 0
                END
            ),
            0
        ) AS totalVolume,

        COUNT(
            CASE
                WHEN ws.isWarmup = 0
                THEN 1
            END
        ) AS totalSets,

        COALESCE(
            SUM(
                CASE
                    WHEN ws.isWarmup = 0
                    THEN ws.reps
                    ELSE 0
                END
            ),
            0
        ) AS totalReps,

        COUNT(DISTINCT s.id) AS sessionsCount

    FROM session_exercises se

    INNER JOIN workout_sets ws
        ON ws.sessionExerciseId = se.id

    INNER JOIN workout_sessions s
        ON se.sessionId = s.id

    WHERE s.userId = :userId
      AND s.isCompleted = 1
      AND (:workoutType IS NULL OR s.workoutType = :workoutType)
      AND (:fromDate IS NULL OR s.date >= :fromDate)

    GROUP BY se.exerciseId, se.exerciseName

    ORDER BY totalVolume DESC
    """
    )
    suspend fun getExerciseStatistics(
        userId: String,
        workoutType: String?,
        fromDate: Long?
    ): List<ExerciseStatisticsRow>

}