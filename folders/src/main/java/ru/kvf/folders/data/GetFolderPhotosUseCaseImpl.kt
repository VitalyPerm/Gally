package ru.kvf.folders.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kvf.core.domain.entities.Photo
import ru.kvf.core.domain.entities.PhotoDate
import ru.kvf.core.domain.repository.PhotosRepository
import ru.kvf.folders.domain.GetFolderPhotosUseCase

class GetFolderPhotosUseCaseImpl(
    private val photosRepository: PhotosRepository
) : GetFolderPhotosUseCase {
    override fun invoke(
        folderName: String
    ): Flow<List<Photo>> = photosRepository.photosFlow.map { allPhotos ->
        allPhotos.filter { it.folder == folderName }.reversed()
    }

    override fun sorted(folderName: String): Flow<Map<PhotoDate, List<Photo>>> =
        photosRepository.photosFlow.map { allPhotos ->
            allPhotos.filter { it.folder == folderName }
                .groupBy(Photo::date).toSortedMap()
        }
}
