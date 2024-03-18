package ru.kvf.core.domain.usecase.favorite

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.utils.FolderList

interface GetFavoriteFoldersUseCase {
    operator fun invoke(): Flow<FolderList>
}
