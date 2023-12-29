package ru.kvf.settings.ui.list

import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import ru.kvf.core.domain.entities.ThemeType
import ru.kvf.core.domain.usecase.LoadPhotosUseCase
import ru.kvf.core.domain.usecase.PhotosSortByUseCase
import ru.kvf.core.ui.VM
import ru.kvf.settings.domain.EdgeToEdgeUseCase
import ru.kvf.settings.domain.ThemeUseCase

class SettingsListViewModel(
    private val themeUseCase: ThemeUseCase,
    private val edgeUseCase: EdgeToEdgeUseCase,
    private val sortByUseCase: PhotosSortByUseCase,
    private val loadPhotosUseCase: LoadPhotosUseCase
) : VM<SettingsListState, SettingsListSideEffect>(SettingsListState()) {

    init {
        collectFlow(themeUseCase.getTheme()) { theme ->
            intent { reduce { state.copy(theme = theme) } }
        }

        collectFlow(edgeUseCase.getEnabled()) { edgeToEdgeEnable ->
            intent { reduce { state.copy(edgeToEdge = edgeToEdgeEnable) } }
        }

        collectFlow(sortByUseCase.get()) { sortBy ->
            intent { reduce { state.copy(sortBy = sortBy) } }
        }
    }

    fun onThemeChanged(themeType: ThemeType) = intent {
        themeUseCase.setThemeType(themeType)
    }

    fun onEdgeToEdgeChanged(enable: Boolean) = intent {
        edgeUseCase.setEnabled(enable)
    }

    fun onSortByChanged(value: Int) = intent {
        sortByUseCase.set(value)
        loadPhotosUseCase()
    }
}
