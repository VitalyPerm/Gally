package ru.kvf.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kvf.core.domain.entities.Setting
import ru.kvf.core.domain.repository.SettingsRepository

private const val EDGE_TO_EDGE_KEY = "edge_to_edge_key"
private const val THEME_KEY = "test1"

class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    override suspend fun updateEdgeToEdgeScroll(enable: Boolean) {
        dataStore.edit { prefs ->
            prefs[booleanPreferencesKey(EDGE_TO_EDGE_KEY)] = enable
        }
    }

    override suspend fun updateTest1(enable: Boolean) {
        dataStore.edit { prefs ->
            prefs[booleanPreferencesKey(THEME_KEY)] = enable
        }
    }

    override fun getSetting(setting: Setting): Flow<Boolean> {
        val key = when (setting) {
            Setting.EdgeToEdge -> EDGE_TO_EDGE_KEY
            Setting.Theme -> THEME_KEY
        }
        return dataStore.data.map {
            it[booleanPreferencesKey(key)] ?: false
        }
    }
}
