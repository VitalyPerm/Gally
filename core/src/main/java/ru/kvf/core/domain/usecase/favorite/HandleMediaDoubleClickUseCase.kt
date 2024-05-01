package ru.kvf.core.domain.usecase.favorite

interface HandleMediaDoubleClickUseCase {
    suspend operator fun invoke(id: Long)
}
