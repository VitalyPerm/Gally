package ru.kvf.settings.domain

import kotlinx.coroutines.flow.Flow

interface EdgeToEdgeUseCase {
    fun getEnabled(): Flow<Boolean>
    suspend fun setEnabled(enabled: Boolean)
}
