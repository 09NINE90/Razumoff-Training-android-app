package ru.razumoff.razumofftraining.di

import android.content.Context
import ru.razumoff.razumofftraining.database.AppDatabase
import ru.razumoff.razumofftraining.database.repository.GymRepository
import ru.razumoff.razumofftraining.utils.DefaultExercisesLoader

/**
 * Контейнер для зависимостей приложения.
 * Создает и хранит синглтоны базы данных и репозитория.
 */
class AppContainer(private val context: Context) {

    // Создаем базу данных (синглтон)
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(context)
    }

    // Создаем репозиторий (синглтон)
    val repository: GymRepository by lazy {
        GymRepository(database)
    }

    suspend fun initializeDefaultExercises(userId: String) {
        try {
            val existingExercises = repository.getExercisesByUserId(userId)

//            repository.deleteAllExercises()

            // Если у пользователя еще нет упражнений, загружаем дефолтные
            if (existingExercises.isEmpty()) {
                val defaultExercises = DefaultExercisesLoader.loadDefaultExercises(context, userId)
                defaultExercises.forEach { exercise ->
                    repository.insertExercise(exercise)
                }
                android.util.Log.d("AppContainer", "✅ Загружено ${defaultExercises.size} дефолтных упражнений")
            }
        } catch (e: Exception) {
            android.util.Log.e("AppContainer", "❌ Ошибка загрузки дефолтных упражнений: ${e.message}", e)
        }
    }
}