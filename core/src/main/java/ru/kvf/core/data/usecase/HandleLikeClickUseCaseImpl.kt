package ru.kvf.core.data.usecase

import ru.kvf.core.domain.repository.LikesRepository
import ru.kvf.core.domain.usecase.HandleLikeClickUseCase

class HandleLikeClickUseCaseImpl(private val likesRepository: LikesRepository) : HandleLikeClickUseCase {
    override suspend fun invoke(id: Long) {
        likesRepository.addToLikedList(id)
    }
}
