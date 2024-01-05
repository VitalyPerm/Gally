package ru.kvf.photos.data

import ru.kvf.core.domain.repository.PhotosRepository
import ru.kvf.photos.domain.LoadPhotosUseCase

class LoadPhotosUseCaseImpl(private val photosRepository: PhotosRepository) :
    LoadPhotosUseCase {
    override suspend fun invoke() {
        photosRepository.loadPhotos()
    }
}
