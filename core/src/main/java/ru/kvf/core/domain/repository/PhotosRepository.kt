package ru.kvf.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.domain.entities.Photo

interface PhotosRepository {

    val photosFlow: Flow<List<Photo>>

    suspend fun loadPhotos()
}
