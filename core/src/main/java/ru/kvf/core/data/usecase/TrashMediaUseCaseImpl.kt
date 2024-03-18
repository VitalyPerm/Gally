package ru.kvf.core.data.usecase

import android.net.Uri
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import ru.kvf.core.domain.usecase.TrashMediaUseCase

class TrashMediaUseCaseImpl : TrashMediaUseCase {
    private val eventChannel = Channel<Pair<Set<Uri>, Boolean>>()
    override fun collect(): Flow<Pair<Set<Uri>, Boolean>> = eventChannel.receiveAsFlow()
    override suspend fun invoke(uris: Set<Uri>, trash: Boolean) {
        eventChannel.send(uris to trash)
    }
}
