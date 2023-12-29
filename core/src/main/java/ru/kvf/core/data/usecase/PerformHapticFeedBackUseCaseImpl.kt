package ru.kvf.core.data.usecase

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import ru.kvf.core.domain.usecase.PerformHapticFeedBackUseCase

class PerformHapticFeedBackUseCaseImpl : PerformHapticFeedBackUseCase {

    private val eventChannel = Channel<Unit>()

    override fun collect(): Flow<Unit> = eventChannel.receiveAsFlow()

    override suspend fun invoke() {
        eventChannel.send(Unit)
    }
}
