package ru.kvf.gally.feature.photos.domain

import kotlinx.coroutines.flow.StateFlow

interface PhotosRepository {

    val data: StateFlow<Pair<List<Photo>, List<Folder>>>

    suspend fun fetch()
}
