package ru.kvf.core.data.usecase.favorite

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.domain.repository.FavoriteRepository
import ru.kvf.core.domain.usecase.favorite.GetFavoriteMediaIdsUseCase
import ru.kvf.core.utils.LongSet

class GetFavoriteMediaIdsUseCaseImpl(
    private val favoriteRepository: FavoriteRepository
) : GetFavoriteMediaIdsUseCase {
    override fun invoke(): Flow<LongSet> = favoriteRepository.getFavoriteMediaIdsFlow()
}
