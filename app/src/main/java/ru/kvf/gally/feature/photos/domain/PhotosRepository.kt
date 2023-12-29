package ru.kvf.gally.feature.photos.domain

interface PhotosRepository {
    suspend fun getPhotos(): List<Photo>

    suspend fun getFolders(): List<Folder>
}
