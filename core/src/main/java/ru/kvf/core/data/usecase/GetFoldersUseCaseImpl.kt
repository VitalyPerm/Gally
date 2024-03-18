package ru.kvf.core.data.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kvf.core.domain.entities.Folder
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.usecase.GetFoldersUseCase
import ru.kvf.core.domain.usecase.GetMediaUseCase
import ru.kvf.core.utils.FolderList

class GetFoldersUseCaseImpl(
    private val getMediaUseCase: GetMediaUseCase
) : GetFoldersUseCase {

    override fun invoke(): Flow<FolderList> = getMediaUseCase().map { media ->
        FolderList(
            media.groupBy(Media::folder).map { (folder, foldermedia) ->
                Folder(
                    id = foldermedia.firstOrNull()?.id ?: 0,
                    name = folder,
                    media = foldermedia
                )
            }.sortedBy { it.name }
        )
    }
}
