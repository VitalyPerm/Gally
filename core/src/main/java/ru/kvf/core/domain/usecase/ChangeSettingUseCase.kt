package ru.kvf.core.domain.usecase

import ru.kvf.core.domain.entities.Setting

interface ChangeSettingUseCase {
    suspend operator fun invoke(setting: Setting, enable: Boolean)
}
