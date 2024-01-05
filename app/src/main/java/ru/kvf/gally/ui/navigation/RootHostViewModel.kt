package ru.kvf.gally.ui.navigation

import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import ru.kvf.core.domain.entities.Setting
import ru.kvf.core.domain.usecase.GetSettingUseCase
import ru.kvf.core.domain.usecase.PerformHapticFeedBackUseCase
import ru.kvf.core.ui.VM
import ru.kvf.photos.domain.LoadPhotosUseCase

class RootHostViewModel(
    getSettingUseCase: GetSettingUseCase,
    performHapticFeedBackUseCase: PerformHapticFeedBackUseCase,
    private val loadPhotosUseCase: LoadPhotosUseCase,
) : VM<RootHostState, RootHostSideEffect>(RootHostState()) {
    init {
        collectFlow(getSettingUseCase(Setting.EdgeToEdge)) { edgeToEdgeEnable ->
            intent {
                reduce { state.copy(edgeToEdgeEnable = edgeToEdgeEnable) }
            }
        }

        collectFlow(performHapticFeedBackUseCase.collect()) {
            intent { postSideEffect(RootHostSideEffect.Vibrate) }
        }
    }

    fun onPause() = intent { reduce { state.copy(loading = true) } }

    fun onResume() = intent {
        safeLaunch { loadPhotosUseCase() }
        reduce { state.copy(loading = false) }
    }
}
