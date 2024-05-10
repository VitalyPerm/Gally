package ru.kvf.core.data.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.MediaDate
import ru.kvf.core.domain.usecase.GetFolderMediaUseCase
import ru.kvf.core.domain.usecase.GetMediaUseCase
import ru.kvf.core.domain.usecase.MediaSortByUseCase
import ru.kvf.core.utils.toCalendarSort
import java.util.Calendar
import java.util.Date

class GetFolderMediaUseCaseImpl(
    private val getMediaUseCase: GetMediaUseCase,
    private val mediaSortByUseCase: MediaSortByUseCase
) : GetFolderMediaUseCase {

    override fun invoke(folderName: String): Flow<Map<MediaDate, List<Media>>> =
        combine(getMediaUseCase(), mediaSortByUseCase.get()) { media, sortBy ->
            media.filter { it.folder == folderName }.map { data ->
                data.copy(
                    date = MediaDate(
                        Calendar.getInstance().apply {
                            time = Date(data.timeStamp)
                        },
                        sortBy.toCalendarSort()
                    )
                )
            }
                .groupBy(Media::date).toSortedMap(Comparator.reverseOrder())
        }
}
