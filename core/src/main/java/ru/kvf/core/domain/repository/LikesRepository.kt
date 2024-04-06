package ru.kvf.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface LikesRepository {
    fun getLikedListFlow(): Flow<Set<Long>>
    suspend fun addToLikedList(id: Long)
}
