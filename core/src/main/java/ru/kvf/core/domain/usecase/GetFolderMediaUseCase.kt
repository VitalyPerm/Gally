package ru.kvf.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.MediaDate

interface GetFolderMediaUseCase {
    operator fun invoke(folderName: String): Flow<Map<MediaDate, List<Media>>>
}
