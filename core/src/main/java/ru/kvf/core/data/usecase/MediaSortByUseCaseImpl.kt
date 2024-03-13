package ru.kvf.core.data.usecase

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kvf.core.domain.usecase.MediaSortByUseCase
import java.util.Calendar

private const val MEDIA_SORT_KEY = "sort_key"

class MediaSortByUseCaseImpl(
    private val dataStore: DataStore<Preferences>,
) : MediaSortByUseCase {

    override suspend fun set(type: Int) {
        dataStore.edit { prefs ->
            prefs[intPreferencesKey(MEDIA_SORT_KEY)] = type
        }
    }

    override fun get(): Flow<Int> = dataStore.data.map {
        it[intPreferencesKey(MEDIA_SORT_KEY)] ?: Calendar.DAY_OF_YEAR
    }
}
