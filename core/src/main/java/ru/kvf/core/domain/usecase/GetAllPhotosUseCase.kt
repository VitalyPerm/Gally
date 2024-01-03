package ru.kvf.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.domain.entities.Photo

interface GetAllPhotosUseCase {
    operator fun invoke(): Flow<List<Photo>>
}
