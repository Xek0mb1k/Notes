package com.mirea.kt.ribo.notes.app

import android.app.Application
import com.mirea.kt.ribo.notes.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        // start Koin
        GlobalContext.startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }

}