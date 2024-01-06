package ru.kvf.photos.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kvf.core.domain.entities.Folder
import ru.kvf.core.domain.entities.Photo
import ru.kvf.core.domain.repository.PhotosRepository
import ru.kvf.photos.domain.GetFoldersUseCase

class GetFoldersUseCaseImpl(
    private val photosRepository: PhotosRepository
) : GetFoldersUseCase {

    override fun invoke(): Flow<List<Folder>> = photosRepository.photosFlow.map { allPhotos ->
        allPhotos.groupBy(Photo::folder).map { (folder, folderPhotos) ->
            Folder(
                id = folderPhotos.firstOrNull()?.id ?: 0,
                name = folder,
                photos = folderPhotos
            )
        }
    }
}
