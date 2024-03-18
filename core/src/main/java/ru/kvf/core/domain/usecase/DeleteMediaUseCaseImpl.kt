package ru.kvf.core.domain.usecase

import android.net.Uri
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class DeleteMediaUseCaseImpl : DeleteMediaUseCase {
    private val eventChannel = Channel<Set<Uri>>()
    override fun collect(): Flow<Set<Uri>> = eventChannel.receiveAsFlow()
    override suspend fun invoke(uris: Set<Uri>) {
        eventChannel.send(uris)
    }
}
