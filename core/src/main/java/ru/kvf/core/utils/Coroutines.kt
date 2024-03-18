package ru.kvf.core.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

fun <T>CoroutineScope.collectFlow(flow: Flow<T>, value: (T) -> Unit) {
    flow.catch { e -> Log.e("Collect flow error! - ${e.message}") }
        .onEach { value(it) }.launchIn(this)
}

fun CoroutineScope.safeLaunch(
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    action: suspend () -> Unit
): Job = launch(dispatcher) {
    try {
        action()
    } catch (e: Exception) {
        Log.e("SafeLaunch error! - ${e.message}")
    }
}
