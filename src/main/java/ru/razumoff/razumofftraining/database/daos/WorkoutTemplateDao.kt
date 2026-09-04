package ru.razumoff.razumofftraining.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.razumoff.razumofftraining.database.entities.WorkoutTemplateEntity
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

    @Query("DELETE FROM workout_templates WHERE id = :templateId AND userId = :userId")
    suspend fun deleteTemplate(templateId: String, userId: String)
}