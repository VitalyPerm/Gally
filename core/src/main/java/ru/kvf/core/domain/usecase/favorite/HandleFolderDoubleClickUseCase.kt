package ru.kvf.core.domain.usecase.favorite

interface HandleFolderDoubleClickUseCase {
    suspend operator fun invoke(id: Long)
}
