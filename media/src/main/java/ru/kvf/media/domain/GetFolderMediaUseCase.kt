package ru.kvf.media.domain

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.MediaDate

interface GetFolderMediaUseCase {
    fun sorted(folderName: String): Flow<Map<MediaDate, List<Media>>>
    operator fun invoke(folderName: String): Flow<List<Media>>
}
