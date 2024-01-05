package ru.kvf.settings

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.kvf.settings.data.ChangeSettingUseCaseImpl
import ru.kvf.settings.domain.ChangeSettingUseCase
import ru.kvf.settings.ui.list.SettingsListViewModel

val settingsModule = module {
    viewModelOf(::SettingsListViewModel)
    single<ChangeSettingUseCase> { ChangeSettingUseCaseImpl(get()) }
}
