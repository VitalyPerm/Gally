package ru.kvf.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.domain.entities.ThemeType

interface ThemeUseCase {
    fun getTheme(): Flow<ThemeType>
    suspend fun setThemeType(theme: ThemeType)
}