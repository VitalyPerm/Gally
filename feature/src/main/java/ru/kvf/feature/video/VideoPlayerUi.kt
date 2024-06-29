package ru.kvf.feature.video

import android.view.View
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayerUi(component: VideoPlayerComponent) {
    var isControllerVisible by remember { mutableStateOf(false) }
    Box {
        AndroidView(
            modifier = Modifier
                .fillMaxSize(),
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = component.player
                    setShowNextButton(false)
                    setShowPreviousButton(false)
                    setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)
                    setControllerVisibilityListener(
                        PlayerView.ControllerVisibilityListener {
                            isControllerVisible = it == View.VISIBLE
                        }
                    )
                }
            }
        )
        AnimatedVisibility(isControllerVisible) {
            Icon(
                Icons.Rounded.Close,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable(onClick = component::onCloseClick)
            )
        }
    }
}
