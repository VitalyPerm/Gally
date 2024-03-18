package ru.kvf.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.domain.entities.Media

interface ShareMediaUseCase {
    fun collect(): Flow<List<Media>>
    suspend operator fun invoke(mediaList: List<Media>)
}
