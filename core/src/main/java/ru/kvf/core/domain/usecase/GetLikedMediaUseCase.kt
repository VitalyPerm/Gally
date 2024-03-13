package ru.kvf.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.domain.entities.Media

interface GetLikedMediaUseCase {
    operator fun invoke(): Flow<List<Media>>
}