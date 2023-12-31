package ru.kvf.core.domain

import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.data.CustomDate

interface PhotosRepository {

    val data: StateFlow<Pair<Map<CustomDate, List<Photo>>, List<Folder>>>

    suspend fun fetch()
}
