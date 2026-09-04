package ru.razumoff.razumofftraining

import android.app.Application
import ru.razumoff.razumofftraining.di.AppContainer

class RazumoffTrainingApplication : Application() {

    // Контейнер зависимостей доступен из любого места приложения
    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        // Инициализируем контейнер с контекстом приложения
        appContainer = AppContainer(this)
    }
}