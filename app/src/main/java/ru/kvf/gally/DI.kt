package ru.kvf.gally

import android.content.Context
import android.content.res.Resources
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.kvf.gally.ui.navigation.RootHostViewModel

private const val DATA_STORE_NAME = "data_store"

val appModule = module {
    single<Resources> { get<Context>().resources }
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create {
            get<Context>().preferencesDataStoreFile(DATA_STORE_NAME)
        }
    }
    viewModelOf(::RootHostViewModel)
}
