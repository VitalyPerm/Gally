package ru.kvf.gally.root

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.core.app.ActivityOptionsCompat
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import ru.kvf.core.domain.entities.ThemeType
import ru.kvf.core.theme.GallyTheme
import ru.kvf.core.utils.createDeleteMediaRequest
import ru.kvf.core.utils.createTrashMediaRequest
import ru.kvf.core.utils.shareMedia
import ru.kvf.feature.folders.details.FolderDetailsUi
import ru.kvf.feature.media.ui.MediaListUi
import ru.kvf.feature.trash.TrashUi
import ru.kvf.gally.home.HomeUi

@Composable
fun RootUi(
    component: RootComponent
) {
    val theme by component.theme.collectAsState()
    val haptic = LocalHapticFeedback.current
    val ctx = LocalContext.current
    val intentSenderLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { _ -> }

    LaunchedEffect(Unit) {
        component.sideEffect.collect {
            when (it) {
                RootComponent.SideEffect.Vibrate ->
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                is RootComponent.SideEffect.ShareMedia -> ctx.shareMedia(it.mediaList)
                is RootComponent.SideEffect.TrashMedia -> {
                    val request = ctx.createTrashMediaRequest(it.uris, it.toTrash)
                    intentSenderLauncher.launch(
                        request,
                        ActivityOptionsCompat.makeTaskLaunchBehind()
                    )
                }

                is RootComponent.SideEffect.DeleteMedia -> {
                    val request = ctx.createDeleteMediaRequest(it.uris)
                    intentSenderLauncher.launch(
                        request,
                        ActivityOptionsCompat.makeTaskLaunchBehind()
                    )
                }
            }
        }
    }

    GallyTheme(
        darkTheme = when (theme) {
            ThemeType.System -> isSystemInDarkTheme()
            ThemeType.Light -> false
            ThemeType.Black -> true
        }
    ) {
        Children(
            stack = component.childStack,
            animation = stackAnimation(scale())
        ) {
            when (val child = it.instance) {
                is RootComponent.Child.Home -> HomeUi(child.component)
                is RootComponent.Child.FolderMediaList -> MediaListUi(child.component)
                is RootComponent.Child.Trash -> TrashUi(child.component)
                is RootComponent.Child.FolderDetails -> FolderDetailsUi(child.component)
            }
        }
    }
}
