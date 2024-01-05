package ru.kvf.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.kvf.core.utils.Log

abstract class VM<STATE : Any, SIDE_EFFECT : Any>(state: STATE) :
    ViewModel(), ContainerHost<STATE, SIDE_EFFECT> {
    override val container: Container<STATE, SIDE_EFFECT> = container(state)

    fun <T>collectFlow(flow: Flow<T>, value: (T) -> Unit) {
        flow.catch { e -> Log.e("Collect flow error! - ${e.message}") }
            .onEach { value(it) }.launchIn(viewModelScope)
    }

    fun safeLaunch(action: suspend () -> Unit): Job = viewModelScope.launch {
        try {
            action()
        } catch (e: Exception) {
            Log.e("SafeLaunch error! - ${e.message}")
        }
    }
}
