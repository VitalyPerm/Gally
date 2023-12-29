package ru.kvf.core.domain.usecase

import kotlinx.coroutines.flow.Flow

interface GetLikedIdsListUseCase {
    operator fun invoke(): Flow<List<Long>>
}
