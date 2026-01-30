package com.example.pmdm.archivebook

import android.app.Application
import com.example.pmdm.archivebook.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class ArchiveBookApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Referencia al contexto de Android para que Koin pueda usarlo
            androidContext(this@ArchiveBookApp)
            // Carga tu archivo AppModule.kt
            modules(appModule)
        }
    }
}