package ru.kvf.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.utils.MediaList

interface MediaRepository {

    val mediaFlow: Flow<List<Media>>
    val trashFlow: Flow<MediaList>

    suspend fun loadMedia()
}
