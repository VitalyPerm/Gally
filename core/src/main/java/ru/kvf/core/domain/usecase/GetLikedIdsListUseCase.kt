package ru.kvf.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.utils.LongSet

interface GetLikedIdsListUseCase {
    operator fun invoke(): Flow<LongSet>
}
