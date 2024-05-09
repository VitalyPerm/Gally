package ru.kvf.core.data.usecase

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.usecase.ShareMediaUseCase

class ShareMediaUseCaseImpl : ShareMediaUseCase {
    private val eventChannel = Channel<List<Media>>()
    override fun collect(): Flow<List<Media>> = eventChannel.receiveAsFlow()
    override suspend fun invoke(mediaList: List<Media>) {
        eventChannel.send(mediaList)
    }
}
