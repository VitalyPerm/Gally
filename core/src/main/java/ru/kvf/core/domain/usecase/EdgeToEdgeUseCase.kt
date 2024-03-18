package ru.kvf.core.domain.usecase

import kotlinx.coroutines.flow.Flow

interface EdgeToEdgeUseCase {
    fun getEnabled(): Flow<Boolean>
    suspend fun setEnabled(enabled: Boolean)
}