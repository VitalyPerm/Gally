package ru.kvf.core.dialog

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface DialogControl<C : Any, T : Any> {
    val dialogSlot: Value<ChildSlot<*, T>>
    val dismissableByUser: StateFlow<Boolean>
    val dismissedEvent: Flow<Unit>

    fun show(config: C)
    fun dismiss()
}
