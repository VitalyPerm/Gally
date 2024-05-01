package ru.kvf.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.utils.LongSet

interface FavoriteRepository {
    fun getFavoriteMediaIdsFlow(): Flow<LongSet>
    suspend fun editFavoriteMedia(id: Long)

    fun getFavoriteFolderIdsFlow(): Flow<LongSet>
    suspend fun editFavoriteFolder(id: Long)
}
