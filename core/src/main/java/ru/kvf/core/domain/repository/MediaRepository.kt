package ru.kvf.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.domain.entities.Media

interface MediaRepository {

    val mediaFlow: Flow<List<Media>>

    suspend fun loadMedia()
}
