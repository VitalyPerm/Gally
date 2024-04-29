package ru.kvf.favorite.ui.folders

import com.arkivanov.decompose.ComponentContext

class RealFavoriteFoldersComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext, FavoriteFoldersComponent
