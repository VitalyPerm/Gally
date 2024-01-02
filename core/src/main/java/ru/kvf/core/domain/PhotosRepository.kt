package ru.kvf.core.domain

import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.data.CustomDate

interface PhotosRepository {

    val photosSortedByDateFlow: StateFlow<Map<CustomDate, List<Photo>>>
    val foldersFlow: StateFlow<List<Folder>>
    val photosFlow: StateFlow<List<Photo>>

    suspend fun fetch()
}
