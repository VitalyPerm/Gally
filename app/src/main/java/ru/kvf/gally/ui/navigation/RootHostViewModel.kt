package ru.kvf.gally.ui.navigation

import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import ru.kvf.core.domain.entities.Setting
import ru.kvf.core.domain.usecase.GetSettingUseCase
import ru.kvf.core.ui.VM
import ru.kvf.photos.domain.LoadPhotosUseCase

class RootHostViewModel(
    getSettingUseCase: GetSettingUseCase,
    private val loadPhotosUseCase: LoadPhotosUseCase,
) : VM<RootHostState, RootHostSideEffect>(RootHostState()) {
    init {
        collectFlow(getSettingUseCase(Setting.EdgeToEdge)) { edgeToEdgeEnable ->
            intent {
                reduce { state.copy(edgeToEdgeEnable = edgeToEdgeEnable) }
            }
        }
    }

    fun loadPhotos() = safeLaunch { loadPhotosUseCase() }
}
