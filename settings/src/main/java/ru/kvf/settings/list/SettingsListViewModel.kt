package ru.kvf.settings.list

import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import ru.kvf.core.domain.entities.Setting
import ru.kvf.core.domain.usecase.ChangeSettingUseCase
import ru.kvf.core.domain.usecase.GetSettingUseCase
import ru.kvf.core.ui.VM

class SettingsListViewModel(
    private val getSettingUseCase: GetSettingUseCase,
    private val changeSettingUseCase: ChangeSettingUseCase
) : VM<SettingsListState, SettingsListSideEffect>(SettingsListState()) {

    init {
        Setting.getAll().forEach { setting ->
            collectFlow(getSettingUseCase(setting)) { enable ->
                updateSetting(setting, enable)
            }
        }
    }

    private fun updateSetting(setting: Setting, enable: Boolean) = intent {
        reduce {
            val settings = state.settings.toMutableList().apply {
                indexOfFirst { it.first == setting }.let { index ->
                    removeAt(index)
                    add(index, setting to enable)
                }
            }
            state.copy(settings = settings)
        }
    }

    fun onSettingChanged(setting: Setting, enable: Boolean) = intent {
        changeSettingUseCase(setting, enable)
    }
}
