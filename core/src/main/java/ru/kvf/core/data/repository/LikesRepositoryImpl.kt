package ru.kvf.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.kvf.core.domain.repository.LikesRepository

private const val LIKE_LIST_KEY = "likes_key"

class LikesRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : LikesRepository {

    override fun getLikedListFlow(): Flow<List<Long>> = dataStore.data.map {
        it[stringPreferencesKey(LIKE_LIST_KEY)].toLikeList()
    }

    override suspend fun addToLikedList(id: Long) {
        dataStore.edit { prefs ->
            val list = prefs[stringPreferencesKey(LIKE_LIST_KEY)].toLikeList().toMutableList()
            if (id in list) {
                list.remove(id)
            } else {
                list.add(id)
            }
            prefs[stringPreferencesKey(LIKE_LIST_KEY)] = list.toLikeString()
        }
    }

    private fun String?.toLikeList(): List<Long> = try {
        Json.decodeFromString<List<Long>>(this!!)
    } catch (e: Exception) {
        emptyList()
    }

    private fun List<Long>.toLikeString(): String = try {
        Json.encodeToString(this)
    } catch (e: Exception) {
        ""
    }
}
