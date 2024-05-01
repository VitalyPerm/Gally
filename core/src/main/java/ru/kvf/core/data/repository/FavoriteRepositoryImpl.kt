package ru.kvf.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.kvf.core.domain.repository.FavoriteRepository
import ru.kvf.core.utils.LongSet

private const val FAVORITE_MEDIA_IDS_KEY = "FAVORITE_MEDIA_IDS_KEY"
private const val FAVORITE_FOLDER_IDS_KEY = "FAVORITE_FOLDER_IDS_KEY"

class FavoriteRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : FavoriteRepository {

    override fun getFavoriteMediaIdsFlow(): Flow<LongSet> = dataStore.data.map {
        it[stringPreferencesKey(FAVORITE_MEDIA_IDS_KEY)].toLongSet()
    }

    override suspend fun editFavoriteMedia(id: Long) = edit(id, FAVORITE_MEDIA_IDS_KEY)

    override fun getFavoriteFolderIdsFlow(): Flow<LongSet> = dataStore.data.map {
        it[stringPreferencesKey(FAVORITE_FOLDER_IDS_KEY)].toLongSet()
    }

    override suspend fun editFavoriteFolder(id: Long) = edit(id, FAVORITE_FOLDER_IDS_KEY)

    private suspend fun edit(id: Long, key: String) {
        dataStore.edit { prefs ->
            val set = prefs[stringPreferencesKey(key)].toLongSet().toMutableSet()
            if (id in set) {
                set.remove(id)
            } else {
                set.add(id)
            }
            prefs[stringPreferencesKey(key)] = set.asString()
        }
    }

    private fun String?.toLongSet(): LongSet = try {
        LongSet(Json.decodeFromString<Set<Long>>(this!!))
    } catch (e: Exception) {
        LongSet.EMPTY
    }

    private fun Set<Long>.asString(): String = try {
        Json.encodeToString(this)
    } catch (e: Exception) {
        ""
    }
}
