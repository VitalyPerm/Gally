package ru.kvf.favorite.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.kvf.core.domain.LikesRepository
import ru.kvf.core.domain.Photo
import ru.kvf.core.domain.PhotosRepository
import ru.kvf.favorite.domain.GetLikedPhotosUseCase

class GetLikedPhotosUseCaseImpl(
    private val photosRepository: PhotosRepository,
    private val likesRepository: LikesRepository
) : GetLikedPhotosUseCase {

    override fun get(): Flow<List<Photo>> = combine(
        photosRepository.photosFlow,
        likesRepository.getLikedListFlow()
    ) { photos, liked ->
        photos.filter { it.id in liked }
    }
}
