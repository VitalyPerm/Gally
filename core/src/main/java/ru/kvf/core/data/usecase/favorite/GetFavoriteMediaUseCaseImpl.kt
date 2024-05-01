package ru.kvf.core.data.usecase.favorite

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.usecase.GetMediaUseCase
import ru.kvf.core.domain.usecase.favorite.GetFavoriteMediaIdsUseCase
import ru.kvf.core.domain.usecase.favorite.GetFavoriteMediaUseCase

class GetFavoriteMediaUseCaseImpl(
    private val getMediaUseCase: GetMediaUseCase,
    private val getFavoriteMediaIdsUseCase: GetFavoriteMediaIdsUseCase
) : GetFavoriteMediaUseCase {

    override fun invoke(): Flow<List<Media>> = combine(
        getMediaUseCase(),
        getFavoriteMediaIdsUseCase()
    ) { media, ids ->
        media.filter { it.id in ids.data }
    }
}
