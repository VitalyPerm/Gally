package ru.kvf.core.data.usecase.favorite

import ru.kvf.core.domain.repository.FavoriteRepository
import ru.kvf.core.domain.usecase.PerformHapticFeedBackUseCase
import ru.kvf.core.domain.usecase.favorite.AddToFavoriteUseCase
import ru.kvf.core.utils.LongSet

class AddToFavoriteUseCaseImpl(
    private val favoriteRepository: FavoriteRepository,
    private val performHapticFeedBackUseCase: PerformHapticFeedBackUseCase
) : AddToFavoriteUseCase {
    override suspend fun invoke(ids: LongSet) {
        favoriteRepository.addMediaToFavorite(ids)
        performHapticFeedBackUseCase()
    }
}
