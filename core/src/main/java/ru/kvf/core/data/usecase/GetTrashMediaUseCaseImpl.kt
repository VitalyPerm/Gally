package ru.kvf.core.data.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.repository.MediaRepository
import ru.kvf.core.domain.usecase.GetTrashMediaUseCase

class GetTrashMediaUseCaseImpl(
    private val mediaRepository: MediaRepository
) : GetTrashMediaUseCase {
    override fun invoke(): Flow<List<Media>> = mediaRepository.mediaFlow.map { list ->
        list.filter { media -> media.isTrashed }
    }
}
