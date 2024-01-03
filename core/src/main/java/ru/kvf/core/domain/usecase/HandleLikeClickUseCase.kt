package ru.kvf.core.domain.usecase

interface HandleLikeClickUseCase {
    suspend operator fun invoke(id: Long)
}
