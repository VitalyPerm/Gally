package ru.kvf.core.domain.usecase

import kotlinx.coroutines.flow.Flow

interface PhotosSortByUseCase {
    suspend fun set(type: Int)
    fun get(): Flow<Int>
}
