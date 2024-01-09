package ru.kvf.photos.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.kvf.core.domain.entities.Photo
import ru.kvf.core.domain.entities.PhotoDate
import ru.kvf.core.domain.repository.PhotosRepository
import ru.kvf.core.domain.usecase.PhotosSortByUseCase
import ru.kvf.core.utils.toCalendarSort
import ru.kvf.photos.domain.GetSortedPhotosUseCase
import java.util.Calendar
import java.util.Comparator
import java.util.Date

class GetSortedPhotosUseCaseImpl(
    private val photosRepository: PhotosRepository,
    private val photosSortByUseCase: PhotosSortByUseCase
) : GetSortedPhotosUseCase {

    override fun invoke(): Flow<Map<PhotoDate, List<Photo>>> =
        combine(photosRepository.photosFlow, photosSortByUseCase.get()) { photos, sortBy ->
            photos.map { photo ->
                photo.copy(
                    date = PhotoDate(
                        Calendar.getInstance().apply {
                            time = Date(photo.timeStamp)
                        },
                        sortBy.toCalendarSort()
                    )
                )
            }.groupBy(Photo::date).toSortedMap(Comparator.reverseOrder())
        }
}
