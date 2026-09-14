package ru.razumoff.razo.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.razumoff.razo.database.entities.WorkoutTemplateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutTemplateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplate(template: WorkoutTemplateEntity)

    @Query("SELECT * FROM workout_templates WHERE userId = :userId ORDER BY name ASC")
    fun getTemplatesByUser(userId: String): Flow<List<WorkoutTemplateEntity>>

    @Query("SELECT * FROM workout_templates WHERE userId = :userId ORDER BY name ASC")
    suspend fun getTemplatesByUserSync(userId: String): List<WorkoutTemplateEntity>

    @Query("SELECT * FROM workout_templates WHERE id = :templateId AND userId = :userId")
    suspend fun getTemplateById(templateId: String, userId: String): WorkoutTemplateEntity?

    @Query("""
        SELECT * FROM workout_templates
        WHERE id = :templateId
          AND userId = :userId
    """)
    fun observeTemplateById(
        templateId: String,
        userId: String
    ): Flow<WorkoutTemplateEntity?>

    @Query("DELETE FROM workout_templates WHERE id = :templateId AND userId = :userId")
    suspend fun deleteTemplate(templateId: String, userId: String)

    @Query("SELECT COUNT(*) FROM workout_templates WHERE userId = :userId")
    fun observeTemplatesCount(userId: String): Flow<Int>

    @Query("""
        UPDATE workout_templates
        SET workoutType = :workoutType,
            updatedAt = :updatedAt
        WHERE id = :templateId
          AND userId = :userId
    """)
    suspend fun updateWorkoutType(
        templateId: String,
        userId: String,
        workoutType: String,
        updatedAt: Long = System.currentTimeMillis()
    )

    @Query("""
        UPDATE workout_templates
        SET name = :name,
            description = :description,
            workoutType = :workoutType,
            updatedAt = :updatedAt
        WHERE id = :templateId
          AND userId = :userId
    """)
    suspend fun updateTemplate(
        templateId: String,
        userId: String,
        name: String,
        description: String?,
        workoutType: String,
        updatedAt: Long = System.currentTimeMillis()
    )
}