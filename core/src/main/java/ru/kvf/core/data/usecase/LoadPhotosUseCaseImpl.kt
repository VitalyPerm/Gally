package ru.kvf.core.data.usecase

import ru.kvf.core.domain.repository.PhotosRepository
import ru.kvf.core.domain.usecase.LoadPhotosUseCase

class LoadPhotosUseCaseImpl(private val photosRepository: PhotosRepository) : LoadPhotosUseCase {
    override suspend fun invoke() {
        photosRepository.loadPhotos()
    }
}
