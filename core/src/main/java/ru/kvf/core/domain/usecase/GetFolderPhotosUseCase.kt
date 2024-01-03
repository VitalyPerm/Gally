package ru.kvf.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.domain.entities.Photo

interface GetFolderPhotosUseCase {
    operator fun invoke(folderName: String): Flow<List<Photo>>
}