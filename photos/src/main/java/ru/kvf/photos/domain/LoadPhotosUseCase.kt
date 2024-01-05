package ru.kvf.photos.domain

interface LoadPhotosUseCase {
    suspend operator fun invoke()
}
