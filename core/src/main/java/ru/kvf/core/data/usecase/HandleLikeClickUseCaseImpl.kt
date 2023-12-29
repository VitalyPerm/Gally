package ru.kvf.core.data.usecase

import kotlinx.coroutines.delay
import ru.kvf.core.domain.repository.LikesRepository
import ru.kvf.core.domain.usecase.HandleLikeClickUseCase
import ru.kvf.core.domain.usecase.PerformHapticFeedBackUseCase
import ru.kvf.core.utils.Constants

class HandleLikeClickUseCaseImpl(
    private val likesRepository: LikesRepository,
    private val performHapticFeedBackUseCase: PerformHapticFeedBackUseCase
) : HandleLikeClickUseCase {
    override suspend fun invoke(id: Long) {
        performHapticFeedBackUseCase()
        delay(Constants.PHOTO_ITEM_LIKE_DURATION)
        likesRepository.addToLikedList(id)
    }
}
