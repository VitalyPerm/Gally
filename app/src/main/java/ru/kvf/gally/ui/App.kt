package ru.kvf.gally.ui

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.VideoFrameDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import org.koin.android.ext.koin.androidContext
import org.koin.core.Koin
import org.koin.core.context.startKoin
import ru.kvf.core.ComponentFactory
import ru.kvf.core.KoinProvider
import ru.kvf.core.coreModule
import ru.kvf.folders.foldersModule
import ru.kvf.gally.appModule
import ru.kvf.media.mediaModule
import ru.kvf.settings.settingsModule

class App : Application(), KoinProvider, ImageLoaderFactory {

    override lateinit var koin: Koin
        private set

    override fun onCreate() {
        super.onCreate()
        koin = startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    appModule,
                    mediaModule,
                    foldersModule,
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
        .components {
            add(VideoFrameDecoder.Factory())
        }
        .build()
}
