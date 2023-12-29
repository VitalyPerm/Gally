package ru.kvf.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface LikesRepository {
    fun getLikedListFlow(): Flow<List<Long>>
    suspend fun addToLikedList(id: Long)
}
