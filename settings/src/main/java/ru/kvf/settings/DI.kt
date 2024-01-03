package ru.kvf.settings

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.kvf.settings.list.SettingsListViewModel

val settingsModule = module {
    viewModelOf(::SettingsListViewModel)
}
