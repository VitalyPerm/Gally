package ru.kvf.photos.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import ru.kvf.core.domain.entities.Photo
import ru.kvf.core.domain.entities.PhotoDate
import ru.kvf.core.domain.repository.PhotosRepository
import ru.kvf.core.domain.usecase.PhotosSortByUseCase
import ru.kvf.core.utils.toCalendarSort
import java.util.Calendar
import java.util.Date

class GetFolderPhotosUseCaseImpl(
    private val photosRepository: PhotosRepository,
    private val photosSortByUseCase: PhotosSortByUseCase
) : ru.kvf.photos.domain.GetFolderPhotosUseCase {
    override fun invoke(
        folderName: String
    ): Flow<List<Photo>> = photosRepository.photosFlow.map { allPhotos ->
        allPhotos.filter { it.folder == folderName }
    }

    override fun sorted(folderName: String): Flow<Map<PhotoDate, List<Photo>>> =
        combine(photosRepository.photosFlow, photosSortByUseCase.get()) { photos, sortBy ->
            photos.filter { it.folder == folderName }.map { photo ->
                photo.copy(
                    date = PhotoDate(
                        Calendar.getInstance().apply {
                            time = Date(photo.timeStamp)
                        },
                        sortBy.toCalendarSort()
                    )
                )
            }.groupBy(Photo::date).toSortedMap(reverseOrder())
        }
}
