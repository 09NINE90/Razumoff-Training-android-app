package ru.razumoff.razo.database.repository

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import ru.razumoff.razo.database.AppDatabase
import ru.razumoff.razo.database.entities.ExerciseEntity
import ru.razumoff.razo.database.entities.SessionExerciseEntity
import ru.razumoff.razo.database.entities.TemplateExerciseEntity
import ru.razumoff.razo.database.entities.UserEntity
import ru.razumoff.razo.database.entities.UserMeasurementEntity
import ru.razumoff.razo.database.entities.WorkoutSessionEntity
import ru.razumoff.razo.database.entities.WorkoutSetEntity
import ru.razumoff.razo.database.entities.WorkoutTemplateEntity
import ru.razumoff.razo.models.ExerciseStatisticsRow
import ru.razumoff.razo.models.WorkoutType

class GymRepository(
    private val database: AppDatabase
) {
    // --- Операции с пользователем ---
    suspend fun getCurrentUser(): UserEntity? {
        return database.userDao().getCurrentUser()
    }

    suspend fun insertOrUpdateUser(user: UserEntity) {
        database.userDao().insertUser(user)
    }

    // --- Замеры пользователя ---
    suspend fun insertMeasurement(measurement: UserMeasurementEntity) {
        database.userMeasurementDao().insertMeasurement(measurement)
    }

    // --- Операции с упражнениями ---
    suspend fun getAllExercises(): List<ExerciseEntity> {
        return database.exerciseDao().getAllExercises()
    }

    fun observeExercisesCount(): Flow<Int> {
        return database.exerciseDao().observeExercisesCount()
    }


    suspend fun getExercisesByUserId(userId: String): List<ExerciseEntity> {
        return database.exerciseDao().getExercisesByUserId(userId)
    }

    suspend fun getExerciseById(exerciseId: String, userId: String): ExerciseEntity? {
        return database.exerciseDao().getExerciseById(exerciseId, userId)
    }

    suspend fun insertExercise(exercise: ExerciseEntity) {
        database.exerciseDao().insertExercise(exercise)
    }

    suspend fun deleteExercise(exercise: ExerciseEntity) {
        database.exerciseDao().deleteExercise(exercise)
    }


    // --- Шаблоны тренировок ---
    suspend fun insertTemplate(template: WorkoutTemplateEntity) {
        database.workoutTemplateDao().insertTemplate(template)
    }

    fun observeTemplatesCount(userId: String): Flow<Int> {
        return database.workoutTemplateDao().observeTemplatesCount(userId)
    }

    suspend fun getTemplatesByUser(userId: String): List<WorkoutTemplateEntity> {
        return database.workoutTemplateDao().getTemplatesByUserSync(userId)
    }

    suspend fun getTemplateById(templateId: String, userId: String): WorkoutTemplateEntity? {
        return database.workoutTemplateDao().getTemplateById(templateId, userId)
    }

    fun observeTemplateById(
        templateId: String,
        userId: String
    ): Flow<WorkoutTemplateEntity?> {
        return database.workoutTemplateDao()
            .observeTemplateById(templateId, userId)
    }

    suspend fun getTemplateExercises(templateId: String): List<TemplateExerciseEntity> {
        return database.templateExerciseDao().getExercisesByTemplateId(templateId)
    }

    suspend fun updateTemplateWorkoutType(
        templateId: String,
        userId: String,
        workoutType: WorkoutType
    ) {
        val type = workoutType.name

        database.workoutTemplateDao().updateWorkoutType(
            templateId = templateId,
            userId = userId,
            workoutType = type
        )

        database.workoutSessionDao().updateWorkoutTypeByTemplate(
            templateId = templateId,
            userId = userId,
            workoutType = type
        )
    }

    suspend fun updateTemplate(
        templateId: String,
        userId: String,
        name: String,
        description: String?,
        workoutType: WorkoutType
    ) {
        database.withTransaction {

            // 1. Обновляем сам шаблон
            database.workoutTemplateDao().updateTemplate(
                templateId = templateId,
                userId = userId,
                name = name,
                description = description
            )

            // 2. Обновляем тип всех связанных сессий
            database.workoutSessionDao().updateWorkoutTypeByTemplate(
                templateId = templateId,
                userId = userId,
                workoutType = workoutType.name
            )

            // 3. Обновляем название всех связанных сессий
            database.workoutSessionDao().updateTemplateNameByTemplate(
                templateId = templateId,
                userId = userId,
                templateName = name
            )
        }
    }

    // --- Упражнения в шаблоне ---
    suspend fun insertTemplateExercises(templateExercises: List<TemplateExerciseEntity>) {
        database.templateExerciseDao().insertTemplateExercises(templateExercises)
    }

    // --- Удаление шаблона ---
    suspend fun deleteTemplate(templateId: String, userId: String) {
        // Сначала удаляем связанные упражнения
        database.templateExerciseDao().deleteExercisesByTemplateId(templateId)
        // Затем удаляем сам шаблон
        database.workoutTemplateDao().deleteTemplate(templateId, userId)
    }

    // --- Сессии тренировок ---
    suspend fun insertSession(session: WorkoutSessionEntity) {
        database.workoutSessionDao().insertSession(session)
    }

    suspend fun getSessionById(sessionId: String, userId: String): WorkoutSessionEntity? {
        return database.workoutSessionDao().getSessionById(sessionId, userId)
    }

    suspend fun completeSession(
        sessionId: String,
        duration: Int?,
        notes: String?,
        feeling: Int?
    ) {
        database.workoutSessionDao().completeSession(
            sessionId,
            duration,
            notes,
            feeling,
            System.currentTimeMillis()
        )
    }

    // --- Упражнения в сессии ---
    suspend fun insertSessionExercise(sessionExercise: SessionExerciseEntity) {
        database.sessionExerciseDao().insertSessionExercise(sessionExercise)
    }

    suspend fun insertSessionExercises(sessionExercises: List<SessionExerciseEntity>) {
        database.sessionExerciseDao().insertSessionExercises(sessionExercises)
    }

    suspend fun getSessionExercises(sessionId: String): List<SessionExerciseEntity> {
        return database.sessionExerciseDao().getExercisesBySessionId(sessionId)
    }


    fun observeLastTenSessionsByUser(userId: String): Flow<List<WorkoutSessionEntity>> {
        return database.workoutSessionDao().observeLastTenSessionsByUser(userId)
    }

    suspend fun updateSessionDate(sessionId: String, newDate: Long) {
        database.workoutSessionDao().updateSessionDate(sessionId, newDate)
    }

    // --- Подходы ---
    suspend fun insertWorkoutSet(workoutSet: WorkoutSetEntity) {
        database.workoutSetDao().insertWorkoutSet(workoutSet)
    }

    suspend fun getSetsBySessionExerciseId(sessionExerciseId: String): List<WorkoutSetEntity> {
        return database.workoutSetDao().getSetsBySessionExerciseId(sessionExerciseId)
    }

    suspend fun deleteWorkoutSet(setId: String) {
        database.workoutSetDao().deleteWorkoutSet(setId)
    }

    suspend fun deleteSetsBySessionExerciseId(sessionExerciseId: String) {
        database.workoutSetDao().deleteSetsBySessionExerciseId(sessionExerciseId)
    }

    // --- Удаление сессии ---
    suspend fun deleteSession(sessionId: String, userId: String) {
        database.workoutSessionDao().deleteSession(sessionId, userId)
    }

    // --- Удаление упражнения сессии ---
    suspend fun deleteSessionExercise(sessionExerciseId: String) {
        database.sessionExerciseDao().deleteSessionExercise(sessionExerciseId)
    }


    // Статистика

    suspend fun getCompletedSessionsCount(
        userId: String,
        workoutType: String?,
        fromDate: Long?
    ): Int {
        return database.workoutSessionDao()
            .getCompletedSessionsCount(
                userId = userId,
                workoutType = workoutType,
                fromDate = fromDate
            )
    }

    suspend fun getTotalSets(
        userId: String,
        workoutType: String?,
        fromDate: Long?
    ): Int {
        return database.workoutSetDao()
            .getTotalSets(
                userId = userId,
                workoutType = workoutType,
                fromDate = fromDate
            )
    }

    suspend fun getTotalReps(
        userId: String,
        workoutType: String?,
        fromDate: Long?
    ): Int {
        return database.workoutSetDao()
            .getTotalReps(
                userId = userId,
                workoutType = workoutType,
                fromDate = fromDate
            )
    }

    suspend fun getTotalVolume(
        userId: String,
        workoutType: String?,
        fromDate: Long?
    ): Double {
        return database.workoutSetDao()
            .getTotalVolume(
                userId = userId,
                workoutType = workoutType,
                fromDate = fromDate
            )
    }

    suspend fun getExerciseStatistics(
        userId: String,
        workoutType: String?,
        fromDate: Long?
    ): List<ExerciseStatisticsRow> {
        return database.workoutSetDao()
            .getExerciseStatistics(
                userId = userId,
                workoutType = workoutType,
                fromDate = fromDate
            )
    }

}