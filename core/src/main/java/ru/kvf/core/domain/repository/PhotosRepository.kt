package ru.kvf.core.domain.repository

import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.domain.entities.Folder
import ru.kvf.core.domain.entities.Photo
import ru.kvf.core.domain.entities.PhotoDate

interface PhotosRepository {

    val photosSortedByDateFlow: StateFlow<Map<PhotoDate, List<Photo>>>
    val foldersFlow: StateFlow<List<Folder>>
    val photosFlow: StateFlow<List<Photo>>

    suspend fun loadPhotos()
}
