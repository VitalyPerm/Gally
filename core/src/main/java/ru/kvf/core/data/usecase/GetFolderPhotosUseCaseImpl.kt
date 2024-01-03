package ru.kvf.core.data.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kvf.core.domain.entities.Photo
import ru.kvf.core.domain.entities.PhotoDate
import ru.kvf.core.domain.repository.PhotosRepository
import ru.kvf.core.domain.usecase.GetFolderPhotosUseCase

class GetFolderPhotosUseCaseImpl(
    private val photosRepository: PhotosRepository
) : GetFolderPhotosUseCase {
    override fun invoke(folderName: String): Flow<Map<PhotoDate, List<Photo>>> =
        photosRepository.photosFlow.map { allPhotos ->
            allPhotos.filter { it.folder == folderName }.reversed()
                .groupBy(Photo::date).toSortedMap()
        }
}
