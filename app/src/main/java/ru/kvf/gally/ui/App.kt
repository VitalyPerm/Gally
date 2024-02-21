package ru.kvf.gally.ui

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import org.koin.android.ext.koin.androidContext
import org.koin.core.Koin
import org.koin.core.context.startKoin
import ru.kvf.core.ComponentFactory
import ru.kvf.core.KoinProvider
import ru.kvf.core.coreModule
import ru.kvf.favorite.favoriteModule
import ru.kvf.folders.foldersModule
import ru.kvf.gally.appModule
import ru.kvf.photos.photosModule
import ru.kvf.settings.settingsModule

class App : Application(), KoinProvider, ImageLoaderFactory {

    override lateinit var koin: Koin
        private set

    private val koinModules = listOf(
        appModule,
        photosModule,
        foldersModule,
        favoriteModule,
        coreModule,
        settingsModule
    )

    override fun onCreate() {
        super.onCreate()
        koin = startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    appModule,
                    photosModule,
                    foldersModule,
                    favoriteModule,
                    coreModule,
                    settingsModule
                )
            )
        }.koin.apply {
            declare(ComponentFactory(this))
        }
    }

    override fun newImageLoader(): ImageLoader = ImageLoader(this).newBuilder()
        .memoryCachePolicy(CachePolicy.ENABLED)
        .memoryCache {
            MemoryCache.Builder(this)
                .strongReferencesEnabled(true)
                .build()
        }
        .diskCachePolicy(CachePolicy.ENABLED)
        .diskCache {
            DiskCache.Builder()
                .maxSizePercent(0.05)
                .directory(cacheDir.resolve("coil_cache"))
                .build()
        }
        .build()
}
