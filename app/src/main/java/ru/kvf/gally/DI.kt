package ru.kvf.gally

import android.content.Context
import android.content.res.Resources
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import org.koin.dsl.module
import ru.kvf.core.ComponentFactory
import ru.kvf.gally.ui.home.HomeComponent
import ru.kvf.gally.ui.home.RealHomeComponent
import ru.kvf.gally.ui.root.RealRootComponent
import ru.kvf.gally.ui.root.RootComponent

private const val DATA_STORE_NAME = "data_store"

val appModule = module {
    single<Resources> { get<Context>().resources }
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create {
            get<Context>().preferencesDataStoreFile(DATA_STORE_NAME)
        }
    }
}

fun ComponentFactory.createRootComponent(
    componentContext: ComponentContext
): RootComponent = RealRootComponent(
    componentContext = componentContext,
    componentFactory = get(),
    themeUseCase = get(),
)

fun ComponentFactory.createHomeComponent(
    componentContext: ComponentContext,
    output: (HomeComponent.Output) -> Unit
): HomeComponent = RealHomeComponent(
    componentContext = componentContext,
    onOutput = output,
    componentFactory = get(),
    loadPhotosUseCase = get(),
    edgeToEdgeUseCase = get(),
    performHapticFeedBackUseCase = get()
)
