package ru.kvf.core.utils

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.max
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

@Composable
fun navigationBarWithImePaddingDp(): Dp {
    val navigationBarPaddingDp = WindowInsets.navigationBars.only(WindowInsetsSides.Bottom)
        .asPaddingValues().calculateBottomPadding()

    val imePaddingDp = WindowInsets.ime.only(WindowInsetsSides.Bottom)
        .asPaddingValues().calculateBottomPadding()

    return max(navigationBarPaddingDp, imePaddingDp)
}
