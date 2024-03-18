package ru.kvf.favorite.ui

sealed interface FavoriteListSideEffect {
    data object ScrollUp : FavoriteListSideEffect
}
