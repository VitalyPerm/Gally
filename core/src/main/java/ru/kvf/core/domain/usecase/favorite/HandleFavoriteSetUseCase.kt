package ru.kvf.core.domain.usecase.favorite

import ru.kvf.core.utils.LongSet

interface HandleFavoriteSetUseCase {
    suspend operator fun invoke(ids: LongSet, add: Boolean)
}
