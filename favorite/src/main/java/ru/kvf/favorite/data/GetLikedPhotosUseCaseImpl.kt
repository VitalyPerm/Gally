package ru.kvf.favorite.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.kvf.core.domain.repository.LikesRepository
import ru.kvf.core.domain.entities.Photo
import ru.kvf.core.domain.repository.PhotosRepository
import ru.kvf.favorite.domain.GetLikedPhotosUseCase

class GetLikedPhotosUseCaseImpl(
    private val photosRepository: PhotosRepository,
    private val likesRepository: LikesRepository
) : GetLikedPhotosUseCase {

    override fun invoke(): Flow<List<Photo>> = combine(
        photosRepository.photosFlow,
        likesRepository.getLikedListFlow()
    ) { photos, liked ->
        photos.filter { it.id in liked }
    }
}
