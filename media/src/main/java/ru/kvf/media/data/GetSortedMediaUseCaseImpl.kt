package ru.kvf.media.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.MediaDate
import ru.kvf.core.domain.repository.MediaRepository
import ru.kvf.core.domain.usecase.MediaSortByUseCase
import ru.kvf.core.utils.toCalendarSort
import ru.kvf.media.domain.GetSortedMediaUseCase
import java.util.Calendar
import java.util.Comparator
import java.util.Date

class GetSortedMediaUseCaseImpl(
    private val mediaRepository: MediaRepository,
    private val mediaSortByUseCase: MediaSortByUseCase
) : GetSortedMediaUseCase {

    override fun invoke(): Flow<Map<MediaDate, List<Media>>> =
        combine(mediaRepository.mediaFlow, mediaSortByUseCase.get()) { media, sortBy ->
            media.map { media ->
                media.copy(
                    date = MediaDate(
                        Calendar.getInstance().apply {
                            time = Date(media.timeStamp)
                        },
                        sortBy.toCalendarSort()
                    )
                )
            }.groupBy(Media::date).toSortedMap(Comparator.reverseOrder())
        }
}
