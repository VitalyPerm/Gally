package ru.kvf.gally.di

import android.content.Context
import android.content.res.Resources
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import org.koin.dsl.module

private const val DATA_STORE_NAME = "data_store"

val appModule = module {
    single<Resources> { get<Context>().resources }
    single<DataStore<Preferences>> {
        val context = get<Context>()
        PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile(DATA_STORE_NAME)
        }
    }
}
