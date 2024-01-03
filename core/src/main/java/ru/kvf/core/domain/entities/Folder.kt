package ru.kvf.core.domain.entities

data class Folder(
    val id: Long,
    val name: String,
    val photos: List<Photo>
)
