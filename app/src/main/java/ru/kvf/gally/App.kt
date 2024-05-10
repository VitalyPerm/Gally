package ru.kvf.gally

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.VideoFrameDecoder
import coil.memory.MemoryCache
import coil.request.CachePolicy
import org.koin.android.ext.koin.androidContext
import org.koin.core.Koin
import org.koin.core.context.startKoin
import ru.kvf.core.ComponentFactory
import ru.kvf.core.KoinProvider
import ru.kvf.core.coreModule
import ru.kvf.featureModule

class App : Application(), KoinProvider, ImageLoaderFactory {

    override lateinit var koin: Koin
        private set

    override fun onCreate() {
        super.onCreate()
        koin = startKoin {
            androidContext(this@App)
            modules(listOf(appModule, coreModule, featureModule))
        }.koin.apply {
            declare(ComponentFactory(this))
        }
    }

    override fun newImageLoader(): ImageLoader = ImageLoader(this).newBuilder()
        .memoryCachePolicy(CachePolicy.ENABLED)
        .memoryCache {
            MemoryCache.Builder(this)
                .maxSizePercent(0.99)
                .build()
        }
        .components {
            add(VideoFrameDecoder.Factory())
        }
        .build()
}
