package ru.kvf.favorite.ui.media

import com.arkivanov.decompose.ComponentContext

class RealFavoriteMediaComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext, FavoriteMediaComponent
