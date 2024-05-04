package ru.kvf.core.domain.usecase.favorite

interface HandleFavoriteClickUseCase {
    suspend operator fun invoke(id: Long)
}
