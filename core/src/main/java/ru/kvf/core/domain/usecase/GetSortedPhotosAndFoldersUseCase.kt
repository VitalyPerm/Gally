package ru.kvf.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.domain.entities.Folder
import ru.kvf.core.domain.entities.Photo
import ru.kvf.core.domain.entities.PhotoDate

interface GetSortedPhotosAndFoldersUseCase {
    operator fun invoke(): Flow<Pair<List<Folder>, Map<PhotoDate, List<Photo>>>>
}
