package ru.kvf.core.data.usecase

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kvf.core.domain.usecase.GridCellsCountChangeUseCase

class GridCellsCountChangeUseCaseImpl(
    private val dataStore: DataStore<Preferences>
) : GridCellsCountChangeUseCase {

    override fun get(screen: GridCellsCountChangeUseCase.Screen): Flow<Int> = dataStore.data.map {
        it[intPreferencesKey(screen.key)] ?: 1
    }

    override suspend fun set(value: Int, screen: GridCellsCountChangeUseCase.Screen) {
        dataStore.edit {
            it[intPreferencesKey(screen.key)] = value
        }
    }
}
