package ru.kvf.core.data.usecase.favorite

import ru.kvf.core.domain.repository.FavoriteRepository
import ru.kvf.core.domain.usecase.PerformHapticFeedBackUseCase
import ru.kvf.core.domain.usecase.favorite.HandleFavoriteClickUseCase

class HandleFavoriteClickUseCaseImpl(
    private val favoriteRepository: FavoriteRepository,
    private val performHapticFeedBackUseCase: PerformHapticFeedBackUseCase
) : HandleFavoriteClickUseCase {
    override suspend fun invoke(id: Long) {
        performHapticFeedBackUseCase()
        favoriteRepository.editFavoriteMedia(id)
    }
}
