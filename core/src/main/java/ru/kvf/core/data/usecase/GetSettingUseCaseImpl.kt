package ru.kvf.core.data.usecase

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.domain.entities.Setting
import ru.kvf.core.domain.repository.SettingsRepository
import ru.kvf.core.domain.usecase.GetSettingUseCase

class GetSettingUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : GetSettingUseCase {
    override fun invoke(setting: Setting): Flow<Boolean> = settingsRepository.getSetting(setting)
}
