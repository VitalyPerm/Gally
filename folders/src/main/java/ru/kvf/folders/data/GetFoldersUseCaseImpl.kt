package ru.kvf.folders.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kvf.core.domain.entities.Folder
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.repository.MediaRepository
import ru.kvf.folders.domain.GetFoldersUseCase

class GetFoldersUseCaseImpl(
    private val mediaRepository: MediaRepository
) : GetFoldersUseCase {

    override fun invoke(): Flow<List<Folder>> = mediaRepository.mediaFlow.map { media ->
        media.groupBy(Media::folder).map { (folder, foldermedia) ->
            Folder(
                id = foldermedia.firstOrNull()?.id ?: 0,
                name = folder,
                media = foldermedia
            )
        }.sortedBy { it.name }
    }
}
