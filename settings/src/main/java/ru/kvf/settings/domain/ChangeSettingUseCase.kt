package ru.kvf.settings.domain

import ru.kvf.core.domain.entities.Setting

interface ChangeSettingUseCase {
    suspend operator fun invoke(setting: Setting, enable: Boolean)
}
