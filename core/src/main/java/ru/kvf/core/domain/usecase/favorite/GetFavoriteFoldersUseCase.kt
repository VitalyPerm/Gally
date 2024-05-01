package ru.kvf.core.domain.usecase.favorite

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.domain.entities.Folder

interface GetFavoriteFoldersUseCase {
    operator fun invoke(): Flow<List<Folder>>
}
