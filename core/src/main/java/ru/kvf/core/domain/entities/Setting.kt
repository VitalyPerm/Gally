package ru.kvf.core.domain.entities

import androidx.annotation.StringRes
import ru.kvf.core.R

sealed class Setting(@StringRes val name: Int) : Comparable<Setting> {
    data object EdgeToEdge : Setting(R.string.setting_edge_to_edge)

    data object Theme : Setting(R.string.setting_theme)

    override fun compareTo(other: Setting): Int = this.name.compareTo(other.name)

    companion object {
        fun getAll() = listOf(EdgeToEdge, Theme)
    }
}
