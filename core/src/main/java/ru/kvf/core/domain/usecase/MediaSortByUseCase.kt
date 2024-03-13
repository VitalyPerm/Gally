package ru.kvf.core.domain.usecase

import kotlinx.coroutines.flow.Flow

interface MediaSortByUseCase {
    suspend fun set(type: Int)
    fun get(): Flow<Int>
}
