package ru.kvf.core.data.usecase.favorite

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.kvf.core.domain.entities.Folder
import ru.kvf.core.domain.usecase.GetFoldersUseCase
import ru.kvf.core.domain.usecase.favorite.GetFavoriteFoldersIdsUseCase
import ru.kvf.core.domain.usecase.favorite.GetFavoriteFoldersUseCase

class GetFavoriteFoldersUseCaseImpl(
    private val getFoldersUseCase: GetFoldersUseCase,
    private val getFavoriteFoldersIdsUseCase: GetFavoriteFoldersIdsUseCase
) : GetFavoriteFoldersUseCase {
    override fun invoke(): Flow<List<Folder>> = combine(
        getFoldersUseCase(),
        getFavoriteFoldersIdsUseCase()
    ) { folders, ids ->
        folders.filter { it.id in ids.data }
    }
}
