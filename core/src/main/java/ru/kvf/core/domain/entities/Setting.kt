package ru.kvf.core.domain.entities

import androidx.annotation.StringRes
import ru.kvf.core.R

sealed class Setting(@StringRes val name: Int) : Comparable<Setting> {
    data object EdgeToEdge : Setting(R.string.setting_edge_to_edge)

    data object Test1 : Setting(R.string.setting_test1)
    data object Test2 : Setting(R.string.setting_test2)

    override fun compareTo(other: Setting): Int = this.name.compareTo(other.name)

    companion object {
        fun getAll() = listOf(EdgeToEdge, Test1, Test2)
    }
}
