package ru.razumoff.razumofftraining.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.razumoff.razumofftraining.database.entities.TemplateExerciseEntity

@Dao
interface TemplateExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplateExercise(templateExercise: TemplateExerciseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplateExercises(templateExercises: List<TemplateExerciseEntity>)

    @Query("SELECT * FROM template_exercises WHERE templateId = :templateId ORDER BY `order` ASC")
    suspend fun getExercisesByTemplateId(templateId: String): List<TemplateExerciseEntity>

    @Query("DELETE FROM template_exercises WHERE templateId = :templateId")
    suspend fun deleteExercisesByTemplateId(templateId: String)
}