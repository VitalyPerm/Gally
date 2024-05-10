package ru.kvf.core.data.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.MediaDate
import ru.kvf.core.domain.usecase.GetMediaUseCase
import ru.kvf.core.domain.usecase.GetSortedMediaUseCase
import ru.kvf.core.domain.usecase.MediaSortByUseCase
import ru.kvf.core.utils.toCalendarSort
import java.util.Calendar
import java.util.Date

class GetSortedMediaUseCaseImpl(
    private val getMediaUseCase: GetMediaUseCase,
    private val mediaSortByUseCase: MediaSortByUseCase
) : GetSortedMediaUseCase {

    override fun invoke(): Flow<Map<MediaDate, List<Media>>> =
        combine(getMediaUseCase(), mediaSortByUseCase.get()) { media, sortBy ->
            media.map {
                it.copy(
                    date = MediaDate(
                        Calendar.getInstance().apply {
                            time = Date(it.timeStamp)
                        },
                        sortBy.toCalendarSort()
                    )
                )
            }.groupBy(Media::date).toSortedMap(reverseOrder()).mapValues { it.value.reversed() }
        }
}
