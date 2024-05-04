package ru.kvf.core.domain.usecase.favorite

import ru.kvf.core.utils.LongSet

interface AddToFavoriteUseCase {
    suspend operator fun invoke(ids: LongSet)
}
