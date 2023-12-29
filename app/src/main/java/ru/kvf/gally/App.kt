package ru.kvf.gally

import android.app.Application
import android.util.Log
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.kvf.gally.di.appModule

class App : Application() {

    companion object {
        fun log(message: String) = Log.d("check___", message)
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}
