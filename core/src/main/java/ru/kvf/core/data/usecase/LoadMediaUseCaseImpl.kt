package ru.kvf.core.data.usecase

import ru.kvf.core.domain.repository.MediaRepository
import ru.kvf.core.domain.usecase.LoadMediaUseCase

class LoadMediaUseCaseImpl(private val mediaRepository: MediaRepository) : LoadMediaUseCase {
    override suspend fun invoke() {
        mediaRepository.loadMedia()
    }
}
