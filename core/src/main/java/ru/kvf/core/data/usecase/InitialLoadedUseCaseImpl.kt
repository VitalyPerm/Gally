package ru.kvf.core.data.usecase

import ru.kvf.core.domain.usecase.InitialLoadedUseCase

class InitialLoadedUseCaseImpl : InitialLoadedUseCase {
    override var isLoading: Boolean = true
}
