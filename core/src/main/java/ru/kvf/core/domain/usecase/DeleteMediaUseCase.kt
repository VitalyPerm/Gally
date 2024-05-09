package ru.kvf.core.domain.usecase

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface DeleteMediaUseCase {
    fun collect(): Flow<Set<Uri>>
    suspend operator fun invoke(uris: Set<Uri>)
}
