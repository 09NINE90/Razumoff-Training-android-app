package ru.razumoff.razumofftraining.database.repository

import kotlinx.coroutines.flow.Flow
import ru.razumoff.razumofftraining.database.AppDatabase
import ru.razumoff.razumofftraining.database.entities.ExerciseEntity
import ru.razumoff.razumofftraining.database.entities.SessionExerciseEntity
import ru.razumoff.razumofftraining.database.entities.TemplateExerciseEntity
import ru.razumoff.razumofftraining.database.entities.UserEntity
import ru.razumoff.razumofftraining.database.entities.UserMeasurementEntity
import ru.razumoff.razumofftraining.database.entities.WorkoutSessionEntity
import ru.razumoff.razumofftraining.database.entities.WorkoutSetEntity
import ru.razumoff.razumofftraining.database.entities.WorkoutTemplateEntity

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

    fun getMeasurements(userId: String): Flow<List<UserMeasurementEntity>> {
        return database.userMeasurementDao().getMeasurements(userId)
    }

    fun getMeasurementsByType(userId: String, type: String): Flow<List<UserMeasurementEntity>> {
        return database.userMeasurementDao().getMeasurementsByType(userId, type)
    }

    suspend fun getLatestMeasurement(userId: String, type: String): UserMeasurementEntity? {
        return database.userMeasurementDao().getLatestMeasurement(userId, type)
    }

    suspend fun deleteMeasurement(id: String, userId: String) {
        database.userMeasurementDao().deleteMeasurement(id, userId)
    }

    // --- Операции с упражнениями ---
    suspend fun getAllExercises(): List<ExerciseEntity> {
        return database.exerciseDao().getAllExercises()
    }

    suspend fun getExercisesCount(): Int {
        return database.exerciseDao().getExercisesCount()
    }

    suspend fun deleteAllExercises() {
        return database.exerciseDao().deleteAllExercises()
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

    suspend fun deleteExerciseById(exerciseId: String, userId: String) {
        database.exerciseDao().deleteExerciseById(exerciseId, userId)
    }

    suspend fun deleteExercise(exercise: ExerciseEntity) {
        database.exerciseDao().deleteExercise(exercise)
    }


    // --- Шаблоны тренировок ---
    suspend fun insertTemplate(template: WorkoutTemplateEntity) {
        database.workoutTemplateDao().insertTemplate(template)
    }

    suspend fun getTemplatesByUser(userId: String): List<WorkoutTemplateEntity> {
        return database.workoutTemplateDao().getTemplatesByUserSync(userId)
    }

    suspend fun getTemplateById(templateId: String, userId: String): WorkoutTemplateEntity? {
        return database.workoutTemplateDao().getTemplateById(templateId, userId)
    }

    suspend fun getTemplateExercises(templateId: String): List<TemplateExerciseEntity> {
        return database.templateExerciseDao().getExercisesByTemplateId(templateId)
    }

    // --- Упражнения в шаблоне ---
    suspend fun insertTemplateExercise(templateExercise: TemplateExerciseEntity) {
        database.templateExerciseDao().insertTemplateExercise(templateExercise)
    }

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

    suspend fun getSessionsByUser(userId: String): List<WorkoutSessionEntity> {
        return database.workoutSessionDao().getSessionsByUser(userId)
    }

    suspend fun getLastTenSessionsByUser(userId: String): List<WorkoutSessionEntity> {
        return database.workoutSessionDao().getLastTenSessionsByUser(userId)
    }

    suspend fun getLastFiveSessionsByUser(userId: String): List<WorkoutSessionEntity> {
        return database.workoutSessionDao().getLastFiveSessionsByUser(userId)
    }

    suspend fun updateSessionDate(sessionId: String, newDate: Long) {
        database.workoutSessionDao().updateSessionDate(sessionId, newDate)
    }

    // --- Подходы ---
    suspend fun insertWorkoutSet(workoutSet: WorkoutSetEntity) {
        database.workoutSetDao().insertWorkoutSet(workoutSet)
    }

    suspend fun insertWorkoutSets(workoutSets: List<WorkoutSetEntity>) {
        database.workoutSetDao().insertWorkoutSets(workoutSets)
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
}