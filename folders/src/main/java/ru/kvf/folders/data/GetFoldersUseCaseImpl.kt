package ru.kvf.folders.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kvf.core.domain.entities.Folder
import ru.kvf.core.domain.entities.Photo
import ru.kvf.core.domain.repository.PhotosRepository

class GetFoldersUseCaseImpl(
    private val photosRepository: PhotosRepository
) : ru.kvf.folders.domain.GetFoldersUseCase {

    override fun invoke(): Flow<List<Folder>> = photosRepository.photosFlow.map { allPhotos ->
        allPhotos.groupBy(Photo::folder).map { (folder, folderPhotos) ->
            Folder(
                id = folderPhotos.firstOrNull()?.id ?: 0,
                name = folder,
                photos = folderPhotos
            )
        }.sortedBy { it.name }
    }
}
