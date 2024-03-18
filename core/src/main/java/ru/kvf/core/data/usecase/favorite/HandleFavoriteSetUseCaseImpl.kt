package ru.kvf.core.data.usecase.favorite

import ru.kvf.core.domain.repository.FavoriteRepository
import ru.kvf.core.domain.usecase.PerformHapticFeedBackUseCase
import ru.kvf.core.domain.usecase.favorite.HandleFavoriteSetUseCase
import ru.kvf.core.utils.LongSet

class HandleFavoriteSetUseCaseImpl(
    private val favoriteRepository: FavoriteRepository,
    private val performHapticFeedBackUseCase: PerformHapticFeedBackUseCase
) : HandleFavoriteSetUseCase {
    override suspend fun invoke(ids: LongSet, add: Boolean) {
        if (add) favoriteRepository.addMediaToFavorite(ids) else favoriteRepository.removeMediaFromFavorite(ids)
        performHapticFeedBackUseCase()
    }
}
