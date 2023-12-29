package ru.kvf.core.data.usecase

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.domain.entities.Photo
import ru.kvf.core.domain.repository.PhotosRepository
import ru.kvf.core.domain.usecase.GetAllPhotosUseCase

class GetAllPhotosUseCaseImpl(
    private val photosRepository: PhotosRepository
) : GetAllPhotosUseCase {
    override fun invoke(): Flow<List<Photo>> = photosRepository.photosFlow
}
