package ru.kvf.core.data.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.kvf.core.domain.repository.LikesRepository
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.repository.MediaRepository
import ru.kvf.core.domain.usecase.GetLikedMediaUseCase

class GetLikedMediaUseCaseImpl(
    private val mediaRepository: MediaRepository,
    private val likesRepository: LikesRepository
) : GetLikedMediaUseCase {

    override fun invoke(): Flow<List<Media>> = combine(
        mediaRepository.mediaFlow,
        likesRepository.getLikedListFlow()
    ) { media, liked ->
        media.filter { it.id in liked }
    }
}
