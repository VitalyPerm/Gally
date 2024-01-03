package ru.kvf.core.data.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.kvf.core.domain.entities.Folder
import ru.kvf.core.domain.entities.Photo
import ru.kvf.core.domain.entities.PhotoDate
import ru.kvf.core.domain.repository.PhotosRepository
import ru.kvf.core.domain.usecase.GetSortedPhotosAndFoldersUseCase

class GetSortedPhotosAndFoldersUseCaseImpl(
    private val photosRepository: PhotosRepository
) : GetSortedPhotosAndFoldersUseCase {
    override operator fun invoke(): Flow<Pair<List<Folder>, Map<PhotoDate, List<Photo>>>> =
        combine(photosRepository.foldersFlow, photosRepository.photosSortedByDateFlow) { folders, photos ->
            folders to photos
        }
}
