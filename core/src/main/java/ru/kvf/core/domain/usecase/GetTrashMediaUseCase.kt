package ru.kvf.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.utils.MediaList

interface GetTrashMediaUseCase {
    operator fun invoke(): Flow<MediaList>
}