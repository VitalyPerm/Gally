package ru.kvf.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.utils.FolderList

interface GetFoldersUseCase {
    operator fun invoke(): Flow<FolderList>
}
