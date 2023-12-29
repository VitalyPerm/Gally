package ru.kvf.gally.ui.navigation

import kotlinx.coroutines.Job
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import ru.kvf.core.domain.usecase.LoadPhotosUseCase
import ru.kvf.core.domain.usecase.PerformHapticFeedBackUseCase
import ru.kvf.core.ui.VM
import ru.kvf.settings.domain.EdgeToEdgeUseCase
import ru.kvf.settings.domain.ThemeUseCase

class RootHostViewModel(
    edgeToEdgeUseCase: EdgeToEdgeUseCase,
    themeUseCase: ThemeUseCase,
    performHapticFeedBackUseCase: PerformHapticFeedBackUseCase,
    private val loadPhotosUseCase: LoadPhotosUseCase,
) : VM<RootHostState, RootHostSideEffect>(RootHostState()) {
    private var loadPhotosJob: Job? = null
    init {
        collectFlow(edgeToEdgeUseCase.getEnabled()) { edgeToEdgeEnable ->
            intent {
                reduce { state.copy(edgeToEdgeEnable = edgeToEdgeEnable) }
            }
        }

        collectFlow(themeUseCase.getTheme()) { theme ->
            intent { reduce { state.copy(theme = theme) } }
        }

        collectFlow(performHapticFeedBackUseCase.collect()) {
            intent { postSideEffect(RootHostSideEffect.Vibrate) }
        }
    }

    fun onPause() = intent { reduce { state.copy(loading = true) } }

    fun onResume() = intent {
        loadPhotos()
        reduce { state.copy(loading = false) }
    }

    private fun loadPhotos() {
        if (loadPhotosJob?.isActive == true) return
        loadPhotosJob = safeLaunch { loadPhotosUseCase() }
    }
}
