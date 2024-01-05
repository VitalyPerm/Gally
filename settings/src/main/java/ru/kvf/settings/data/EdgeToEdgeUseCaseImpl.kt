package ru.kvf.settings.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kvf.settings.domain.EdgeToEdgeUseCase

private const val EDGE_TO_EDGE_KEY = "edge_to_edge_key"

class EdgeToEdgeUseCaseImpl(
    private val dataStore: DataStore<Preferences>
) : EdgeToEdgeUseCase {
    override fun getEnabled(): Flow<Boolean> = dataStore.data.map {
        it[booleanPreferencesKey(EDGE_TO_EDGE_KEY)] ?: false
    }

    override suspend fun setEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[booleanPreferencesKey(EDGE_TO_EDGE_KEY)] = enabled
        }
    }
}
