package ru.kvf.core.domain.usecase

import kotlinx.coroutines.flow.Flow

interface PerformHapticFeedBackUseCase {
    fun collect(): Flow<Unit>
    suspend operator fun invoke()
}
