package ru.kvf.feature.favorite.folders

import com.arkivanov.decompose.ComponentContext

class RealFavoriteFoldersComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext, FavoriteFoldersComponent
