package ru.kvf.core.domain.usecase

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface TrashMediaUseCase {
    fun collect(): Flow<Pair<Set<Uri>, Boolean>>
    suspend operator fun invoke(uris: Set<Uri>, trash: Boolean)
}
