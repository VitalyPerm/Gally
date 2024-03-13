package ru.kvf.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.domain.entities.Media

interface GetMediaUseCase {
    operator fun invoke(): Flow<List<Media>>
}
