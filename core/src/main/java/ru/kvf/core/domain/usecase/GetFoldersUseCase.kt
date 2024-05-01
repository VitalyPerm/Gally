package ru.kvf.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.domain.entities.Folder

interface GetFoldersUseCase {
    operator fun invoke(): Flow<List<Folder>>
}
