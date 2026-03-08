package com.example.tuned_launcher_app

import android.app.Application
import com.example.tuned_launcher_app.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TunedLauncherApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TunedLauncherApp)
            modules(appModule)
        }
    }
}