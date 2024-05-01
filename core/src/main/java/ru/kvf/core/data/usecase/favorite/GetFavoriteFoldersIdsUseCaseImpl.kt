package ru.kvf.core.data.usecase.favorite

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.domain.repository.FavoriteRepository
import ru.kvf.core.domain.usecase.favorite.GetFavoriteFoldersIdsUseCase
import ru.kvf.core.utils.LongSet

class GetFavoriteFoldersIdsUseCaseImpl(
    private val favoriteRepository: FavoriteRepository
) : GetFavoriteFoldersIdsUseCase {
    override fun invoke(): Flow<LongSet> = favoriteRepository.getFavoriteFolderIdsFlow()
}
