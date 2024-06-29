package ru.kvf.feature.video

import androidx.media3.exoplayer.ExoPlayer

interface VideoPlayerComponent {
    val player: ExoPlayer

    fun onCloseClick()

    sealed interface Output {
        data object CloseRequested : Output
    }
}
