package ru.kvf.core.data.usecase

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.repository.MediaRepository
import ru.kvf.core.domain.usecase.GetMediaUseCase

class GetMediaUseCaseImpl(
    private val mediaRepository: MediaRepository
) : GetMediaUseCase {
    override fun invoke(): Flow<List<Media>> = mediaRepository.mediaFlow
}
