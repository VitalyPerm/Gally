package ru.kvf.settings.data

import ru.kvf.core.domain.entities.Setting
import ru.kvf.core.domain.repository.SettingsRepository
import ru.kvf.settings.domain.ChangeSettingUseCase

class ChangeSettingUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : ChangeSettingUseCase {

    override suspend fun invoke(setting: Setting, enable: Boolean) {
        when (setting) {
            Setting.EdgeToEdge -> settingsRepository.updateEdgeToEdgeScroll(enable)
            Setting.Test1 -> settingsRepository.updateTest1(enable)
            Setting.Test2 -> settingsRepository.updateTest2(enable)
        }
    }
}
