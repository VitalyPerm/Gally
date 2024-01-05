package ru.kvf.core.domain.entities

import androidx.annotation.StringRes
import ru.kvf.core.R

enum class ThemeType {
    System, Light, Black;

    @StringRes fun getRes() = when (this) {
        System -> R.string.theme_system
        Light -> R.string.theme_light
        Black -> R.string.theme_dark
    }

    fun toInt() = when (this) {
        System -> 0
        Light -> 1
        Black -> 2
    }

    companion object {
        fun fromInt(value: Int?) = when (value) {
            2 -> Black
            1 -> Light
            else -> System
        }
    }
}
