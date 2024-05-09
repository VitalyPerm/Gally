package ru.kvf.core.data.usecase

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.domain.repository.MediaRepository
import ru.kvf.core.domain.usecase.GetTrashMediaUseCase
import ru.kvf.core.utils.MediaList

class GetTrashMediaUseCaseImpl(
    private val mediaRepository: MediaRepository
) : GetTrashMediaUseCase {
    override fun invoke(): Flow<MediaList> = mediaRepository.trashFlow
}
