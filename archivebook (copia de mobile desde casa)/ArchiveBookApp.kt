package com.example.pmdm.archivebook

import android.app.Application
import com.example.pmdm.archivebook.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class ArchiveBookApp : Application() { // Nombre específico es mejor
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // 1. El Logger es vital en desarrollo para ver si Koin carga bien los módulos
            androidLogger()

            // 2. Proporciona el contexto de la aplicación
            androidContext(this@ArchiveBookApp)

            // 3. Tus módulos
            modules(appModule)
        }
    }
}