package ru.kvf.core.data.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.MediaDate
import ru.kvf.core.domain.repository.MediaRepository
import ru.kvf.core.domain.usecase.GetFolderMediaUseCase
import ru.kvf.core.domain.usecase.MediaSortByUseCase
import ru.kvf.core.utils.toCalendarSort
import java.util.Calendar
import java.util.Date

class GetFolderMediaUseCaseImpl(
    private val mediaRepository: MediaRepository,
    private val mediaSortByUseCase: MediaSortByUseCase
) : GetFolderMediaUseCase {
    override fun invoke(
        folderName: String
    ): Flow<List<Media>> = mediaRepository.mediaFlow.map { media ->
        media.filter { it.folder == folderName }
    }

    override fun sorted(folderName: String): Flow<Map<MediaDate, List<Media>>> =
        combine(mediaRepository.mediaFlow, mediaSortByUseCase.get()) { media, sortBy ->
            media.filter { it.folder == folderName }.map { data ->
                data.copy(
                    date = MediaDate(
                        Calendar.getInstance().apply {
                            time = Date(data.timeStamp)
                        },
                        sortBy.toCalendarSort()
                    )
                )
            }.groupBy(Media::date).toSortedMap(reverseOrder())
        }
}
