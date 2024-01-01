package ru.kvf.gally

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.request.CachePolicy
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.kvf.gally.di.appModule

class App : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }

    override fun newImageLoader(): ImageLoader = ImageLoader(this).newBuilder()
        .diskCachePolicy(CachePolicy.ENABLED)
        .diskCache {
            DiskCache.Builder()
                .maxSizePercent(0.1)
                .directory(cacheDir)
                .build()
        }
        .build()
}
