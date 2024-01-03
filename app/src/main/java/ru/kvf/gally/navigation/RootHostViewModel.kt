package ru.kvf.gally.navigation

import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import ru.kvf.core.domain.entities.Setting
import ru.kvf.core.domain.usecase.GetSettingUseCase
import ru.kvf.core.ui.VM

class RootHostViewModel(
    getSettingUseCase: GetSettingUseCase
) : VM<RootHostState, RootHostSideEffect>(RootHostState()) {
    init {
        collectFlow(getSettingUseCase(Setting.EdgeToEdge)) { edgeToEdgeEnable ->
            intent {
                reduce { state.copy(edgeToEdgeEnable = edgeToEdgeEnable) }
            }
        }
    }
}
