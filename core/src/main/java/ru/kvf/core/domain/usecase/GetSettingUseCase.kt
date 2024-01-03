package ru.kvf.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.domain.entities.Setting

interface GetSettingUseCase {
    operator fun invoke(setting: Setting): Flow<Boolean>
}
