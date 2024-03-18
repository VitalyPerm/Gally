package ru.kvf.settings.ui.list

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import ru.kvf.core.domain.entities.ThemeType
import ru.kvf.core.domain.usecase.LoadMediaUseCase
import ru.kvf.core.domain.usecase.MediaSortByUseCase
import ru.kvf.core.utils.coroutineScope
import ru.kvf.core.utils.safeLaunch
import ru.kvf.settings.domain.EdgeToEdgeUseCase
import ru.kvf.settings.domain.ThemeUseCase
import java.util.Calendar

class RealSettingsListComponent(
    componentContext: ComponentContext,
    private val themeUseCase: ThemeUseCase,
    private val edgeUseCase: EdgeToEdgeUseCase,
    private val sortByUseCase: MediaSortByUseCase,
    private val loadMediaUseCase: LoadMediaUseCase
) : ComponentContext by componentContext, SettingsListComponent {

    private val componentScope = lifecycle.coroutineScope()

    override val edgeToEdgeEnable = edgeUseCase.getEnabled()
        .stateIn(componentScope, SharingStarted.Lazily, false)
    override val sortBy = sortByUseCase.get()
        .stateIn(componentScope, SharingStarted.Lazily, Calendar.DAY_OF_YEAR)
    override val theme = themeUseCase.getTheme()
        .stateIn(componentScope, SharingStarted.Lazily, ThemeType.System)

    override fun onEdgeToEdgeChanged(enable: Boolean) {
        componentScope.safeLaunch { edgeUseCase.setEnabled(enable) }
    }

    override fun onSortByChanged(value: Int) {
        componentScope.safeLaunch {
            sortByUseCase.set(value)
            loadMediaUseCase()
        }
    }

    override fun onThemeChanged(themeType: ThemeType) {
        componentScope.safeLaunch { themeUseCase.setThemeType(themeType) }
    }
}
