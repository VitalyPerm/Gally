package ru.kvf.core.data.usecase.favorite

import kotlinx.coroutines.delay
import ru.kvf.core.domain.repository.FavoriteRepository
import ru.kvf.core.domain.usecase.favorite.HandleFavoriteClickUseCase
import ru.kvf.core.domain.usecase.PerformHapticFeedBackUseCase
import ru.kvf.core.utils.Constants

class HandleFavoriteClickUseCaseImpl(
    private val favoriteRepository: FavoriteRepository,
    private val performHapticFeedBackUseCase: PerformHapticFeedBackUseCase
) : HandleFavoriteClickUseCase {
    override suspend fun invoke(id: Long) {
        performHapticFeedBackUseCase()
        delay(Constants.MEDIA_ITEM_LIKE_DURATION)
        favoriteRepository.editFavoriteMedia(id)
    }
}
