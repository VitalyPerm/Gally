package ru.kvf.folders.domain

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.domain.entities.Photo
import ru.kvf.core.domain.entities.PhotoDate

interface GetFolderPhotosUseCase {
    fun sorted(folderName: String): Flow<Map<PhotoDate, List<Photo>>>
    operator fun invoke(folderName: String): Flow<List<Photo>>
}
