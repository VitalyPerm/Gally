package ru.kvf.settings.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kvf.core.domain.entities.ThemeType
import ru.kvf.settings.domain.ThemeUseCase

private const val THEME_KEY = "theme_key"

class ThemeUseCaseImpl(
    private val dataStore: DataStore<Preferences>
) : ThemeUseCase {

    override fun getTheme(): Flow<ThemeType> = dataStore.data.map { prefs ->
        ThemeType.fromInt(prefs[intPreferencesKey(THEME_KEY)])
    }

    override suspend fun setThemeType(theme: ThemeType) {
        dataStore.edit { prefs ->
            prefs[intPreferencesKey(THEME_KEY)] = theme.toInt()
        }
    }
}
