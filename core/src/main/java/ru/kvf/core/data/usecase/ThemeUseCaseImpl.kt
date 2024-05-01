package ru.kvf.core.data.usecase

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kvf.core.domain.entities.ThemeType
import ru.kvf.core.domain.usecase.ThemeUseCase

class ThemeUseCaseImpl(
    private val dataStore: DataStore<Preferences>
) : ThemeUseCase {

    private companion object {
        const val THEME_KEY = "THEME_KEY"
    }

    override fun getTheme(): Flow<ThemeType> = dataStore.data.map { prefs ->
        ThemeType.fromInt(prefs[intPreferencesKey(THEME_KEY)])
    }

    override suspend fun setThemeType(theme: ThemeType) {
        dataStore.edit { prefs ->
            prefs[intPreferencesKey(THEME_KEY)] = theme.toInt()
        }
    }
}
