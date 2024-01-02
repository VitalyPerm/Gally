package ru.kvf.gally

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.kvf.core.coreModule
import ru.kvf.gally.di.appModule
import ru.kvf.photos.photosModule

class App : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    appModule,
                    photosModule,
                    coreModule
                )
            )
        }
    }

    override fun newImageLoader(): ImageLoader = ImageLoader(this).newBuilder()
        .memoryCachePolicy(CachePolicy.ENABLED)
        .memoryCache {
            MemoryCache.Builder(this)
                .maxSizePercent(0.2)
                .build()
        }
        .diskCachePolicy(CachePolicy.ENABLED)
        .diskCache {
            DiskCache.Builder()
                .maxSizePercent(0.1)
                .directory(cacheDir)
                .build()
        }
        .build()
}
