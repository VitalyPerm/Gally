package ru.kvf.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.domain.entities.Setting

interface SettingsRepository {
    suspend fun updateEdgeToEdgeScroll(enable: Boolean)
    suspend fun updateTest1(enable: Boolean)
    suspend fun updateTest2(enable: Boolean)
    fun getSetting(setting: Setting): Flow<Boolean>
}
