package ru.kvf.core.data.usecase.favorite

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.kvf.core.domain.usecase.GetFoldersUseCase
import ru.kvf.core.domain.usecase.favorite.GetFavoriteFoldersIdsUseCase
import ru.kvf.core.domain.usecase.favorite.GetFavoriteFoldersUseCase
import ru.kvf.core.utils.FolderList

class GetFavoriteFoldersUseCaseImpl(
    private val getFoldersUseCase: GetFoldersUseCase,
    private val getFavoriteFoldersIdsUseCase: GetFavoriteFoldersIdsUseCase
) : GetFavoriteFoldersUseCase {
    override fun invoke(): Flow<FolderList> = combine(
        getFoldersUseCase(),
        getFavoriteFoldersIdsUseCase()
    ) { folders, ids ->
        FolderList(folders.data.filter { it.id in ids.data })
    }
}
