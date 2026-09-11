package ru.razumoff.razo.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.razumoff.razo.database.entities.SessionExerciseEntity

@Dao
interface SessionExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSessionExercise(sessionExercise: SessionExerciseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSessionExercises(sessionExercises: List<SessionExerciseEntity>)

    @Query("SELECT * FROM session_exercises WHERE sessionId = :sessionId ORDER BY `order` ASC")
    suspend fun getExercisesBySessionId(sessionId: String): List<SessionExerciseEntity>

    @Query("DELETE FROM session_exercises WHERE sessionId = :sessionId")
    suspend fun deleteExercisesBySessionId(sessionId: String)

    @Query("DELETE FROM session_exercises WHERE id = :sessionExerciseId")
    suspend fun deleteSessionExercise(sessionExerciseId: String)

    @Query("DELETE FROM session_exercises WHERE sessionId = :sessionId")
    suspend fun deleteSessionExercisesBySessionId(sessionId: String)
}