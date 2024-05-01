package ru.kvf.core.domain.usecase.favorite

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.utils.LongSet

interface GetFavoriteMediaIdsUseCase {
    operator fun invoke(): Flow<LongSet>
}
