package ru.kvf.core.domain.usecase

interface PhotosSortByUseCase {
    suspend fun set(type: Int)
    suspend fun get(): Int
}
