package ru.kvf.core.domain.usecase.favorite

interface HandleFolderFavoriteClickUseCase {
    suspend operator fun invoke(id: Long)
}
