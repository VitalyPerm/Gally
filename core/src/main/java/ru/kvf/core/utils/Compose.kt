package ru.kvf.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Composable
fun <T> Flow<T>.collectSideEffect(
    sideEffect: (suspend (sideEffect: T) -> Unit)
) {
    val lifeCycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(this, lifeCycleOwner) {
        lifeCycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                collect { sideEffect(it) }
            }
        }
    }
}
