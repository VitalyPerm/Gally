package ru.kvf.core.dialog

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.KSerializer
import ru.kvf.core.utils.coroutineScope

fun <C : Any, T : Any> ComponentContext.dialogControl(
    key: String,
    dialogComponentFactory: (C, ComponentContext, DialogControl<C, T>) -> T,
    dismissableByUser: (C, T) -> StateFlow<Boolean> = { _, _ -> MutableStateFlow(true) },
    serializer: KSerializer<C>? = null,
): DialogControl<C, T> {
    return RealDialogControl(
        componentContext = this,
        dialogComponentFactory = dialogComponentFactory,
        key = key,
        dismissableByUser = dismissableByUser,
        serializer = serializer
    )
}

private class RealDialogControl<C : Any, T : Any>(
    private val componentContext: ComponentContext,
    key: String,
    private val dialogComponentFactory: (C, ComponentContext, DialogControl<C, T>) -> T,
    dismissableByUser: (C, T) -> StateFlow<Boolean>,
    serializer: KSerializer<C>?
) : DialogControl<C, T> {

    private val dialogNavigation = SlotNavigation<C>()
    private val scope = componentContext.lifecycle.coroutineScope()

    override val dialogSlot: Value<ChildSlot<C, T>> =
        componentContext.childSlot<C, T>(
            source = dialogNavigation,
            handleBackButton = false,
            serializer = serializer,
            key = key,
            childFactory = { config: C, context: ComponentContext ->
                dialogComponentFactory(config, context, this)
            }
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    override val dismissableByUser: StateFlow<Boolean> = getDialogSlotStateFlow()
        .flatMapLatest { slot ->
            slot.child?.let { dismissableByUser(it.configuration, it.instance) }
                ?: MutableStateFlow(false)
        }
        .stateIn(scope, SharingStarted.Eagerly, initialValue = false)

    private fun getDialogSlotStateFlow(): MutableStateFlow<ChildSlot<C, T>> {
        val state = MutableStateFlow(dialogSlot.value)
        if (componentContext.lifecycle.state != Lifecycle.State.DESTROYED) {
            val disposable = dialogSlot.observe { state.value = it }
            componentContext.lifecycle.doOnDestroy { disposable.cancel() }
        }
        return state
    }

    override val dismissedEvent = MutableSharedFlow<Unit>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override fun show(config: C) {
        dialogNavigation.activate(config)
    }

    override fun dismiss() {
        if (dialogSlot.value.child == null) return
        dialogNavigation.dismiss()
        dismissedEvent.tryEmit(Unit)
    }
}
