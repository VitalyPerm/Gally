package ru.kvf.core.data.usecase

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.domain.repository.LikesRepository
import ru.kvf.core.domain.usecase.GetLikedIdsListUseCase

class GetLikedIdsListUseCaseImpl(
    private val likesRepository: LikesRepository
) : GetLikedIdsListUseCase {
    override fun invoke(): Flow<List<Long>> = likesRepository.getLikedListFlow()
}
