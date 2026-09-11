package ru.razumoff.razo

import android.app.Application
import ru.razumoff.razo.di.AppContainer

class RazoApplication : Application() {

    // Контейнер зависимостей доступен из любого места приложения
    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        // Инициализируем контейнер с контекстом приложения
        appContainer = AppContainer(this)
    }
}