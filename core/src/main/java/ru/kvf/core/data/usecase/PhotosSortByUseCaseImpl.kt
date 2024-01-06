package ru.kvf.core.data.usecase

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kvf.core.domain.usecase.PhotosSortByUseCase
import java.util.Calendar

private const val PHOTOS_SORT_KEY = "sort_key"

class PhotosSortByUseCaseImpl(
    private val dataStore: DataStore<Preferences>,
) : PhotosSortByUseCase {

    override suspend fun set(type: Int) {
        dataStore.edit { prefs ->
            prefs[intPreferencesKey(PHOTOS_SORT_KEY)] = type
        }
    }

    override fun get(): Flow<Int> = dataStore.data.map {
        it[intPreferencesKey(PHOTOS_SORT_KEY)] ?: Calendar.DAY_OF_YEAR
    }
}
