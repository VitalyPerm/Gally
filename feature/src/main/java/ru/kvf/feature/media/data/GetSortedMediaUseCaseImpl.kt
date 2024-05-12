package ru.kvf.feature.media.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.MediaDate
import ru.kvf.core.domain.entities.MimeType
import ru.kvf.core.domain.usecase.GetMediaUseCase
import ru.kvf.core.domain.usecase.MediaSortByUseCase
import ru.kvf.core.utils.toCalendarSort
import ru.kvf.feature.media.domain.GetSortedMediaUseCase
import ru.kvf.feature.media.domain.MediaFilterUseCase
import java.util.Calendar
import java.util.Date

class GetSortedMediaUseCaseImpl(
    private val getMediaUseCase: GetMediaUseCase,
    private val mediaSortByUseCase: MediaSortByUseCase,
    private val mediaFilterUseCase: MediaFilterUseCase
) : GetSortedMediaUseCase {

    override fun invoke(): Flow<Map<MediaDate, List<Media>>> =
        combine(
            getMediaUseCase(),
            mediaSortByUseCase.get(),
            mediaFilterUseCase.get()
        ) { media, sortBy, (photo, video) ->
            val filtered = media.filter {
                when {
                    photo && video -> true
                    !photo && !video -> false
                    photo -> it.mimeType == MimeType.Photo
                    else -> it.mimeType == MimeType.Video
                }
            }
            filtered.map {
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
