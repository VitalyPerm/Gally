package ru.kvf.settings

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.kvf.settings.data.EdgeToEdgeUseCaseImpl
import ru.kvf.settings.data.ThemeUseCaseImpl
import ru.kvf.settings.domain.EdgeToEdgeUseCase
import ru.kvf.settings.domain.ThemeUseCase
import ru.kvf.settings.ui.list.SettingsListViewModel

val settingsModule = module {
    viewModelOf(::SettingsListViewModel)
    single<EdgeToEdgeUseCase> { EdgeToEdgeUseCaseImpl(get()) }
    single<ThemeUseCase> { ThemeUseCaseImpl(get()) }
}
