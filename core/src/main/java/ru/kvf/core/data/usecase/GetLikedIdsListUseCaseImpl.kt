package ru.kvf.core.data.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kvf.core.domain.repository.LikesRepository
import ru.kvf.core.domain.usecase.GetLikedIdsListUseCase
import ru.kvf.core.utils.LongSet

class GetLikedIdsListUseCaseImpl(
    private val likesRepository: LikesRepository
) : GetLikedIdsListUseCase {
    override fun invoke(): Flow<LongSet> = likesRepository.getLikedListFlow().map { LongSet.from(it) }
}
