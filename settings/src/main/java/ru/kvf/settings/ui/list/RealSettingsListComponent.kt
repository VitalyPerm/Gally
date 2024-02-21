package ru.kvf.settings.ui.list

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import ru.kvf.core.domain.entities.ThemeType
import ru.kvf.core.domain.usecase.LoadPhotosUseCase
import ru.kvf.core.domain.usecase.PhotosSortByUseCase
import ru.kvf.core.utils.collectFlow
import ru.kvf.core.utils.componentCoroutineScope
import ru.kvf.core.utils.safeLaunch
import ru.kvf.settings.domain.EdgeToEdgeUseCase
import ru.kvf.settings.domain.ThemeUseCase

class RealSettingsListComponent(
    componentContext: ComponentContext,
    private val themeUseCase: ThemeUseCase,
    private val edgeUseCase: EdgeToEdgeUseCase,
    private val sortByUseCase: PhotosSortByUseCase,
    private val loadPhotosUseCase: LoadPhotosUseCase
) : ComponentContext by componentContext, SettingsListComponent {

    private val scope = componentCoroutineScope()
    override val state = MutableStateFlow(SettingsListState())

    init {
        scope.collectFlow(themeUseCase.getTheme()) { theme ->
            state.update { state.value.copy(theme = theme) }
        }

        scope.collectFlow(edgeUseCase.getEnabled()) { edgeToEdgeEnable ->
            state.update { state.value.copy(edgeToEdge = edgeToEdgeEnable) }
        }

        scope.collectFlow(sortByUseCase.get()) { sortBy ->
            state.update { state.value.copy(sortBy = sortBy) }
        }
    }

    override fun onEdgeToEdgeChanged(enable: Boolean) {
        scope.safeLaunch { edgeUseCase.setEnabled(enable) }
    }

    override fun onSortByChanged(value: Int) {
        scope.safeLaunch {
            sortByUseCase.set(value)
            loadPhotosUseCase()
        }
    }

    override fun onThemeChanged(themeType: ThemeType) {
        scope.safeLaunch { themeUseCase.setThemeType(themeType) }
    }
}
