package ru.kvf.gally.feature.photos.domain

data class Folder(
    val id: Long,
    val name: String,
    val photos: List<Photo>
)
