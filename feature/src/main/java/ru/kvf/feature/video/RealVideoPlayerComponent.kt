package ru.kvf.feature.video

import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import ru.kvf.core.domain.usecase.GetMediaUseCase
import ru.kvf.core.utils.coroutineScope

class RealVideoPlayerComponent(
    componentContext: ComponentContext,
    private val onOutput: (VideoPlayerComponent.Output) -> Unit,
    mediaId: Long,
    override val player: ExoPlayer,
    getMediaUseCase: GetMediaUseCase
) : ComponentContext by componentContext, VideoPlayerComponent {

    private val componentScope = coroutineScope()

    init {
        componentScope.launch {
            getMediaUseCase().firstOrNull()?.firstOrNull { it.id == mediaId }?.uri?.let { uri ->
                player.apply {
                    setMediaItem(MediaItem.fromUri(uri))
                    playWhenReady = true
                    prepare()
                }
            }
        }
        lifecycle.doOnDestroy { player.release() }
    }

    override fun onCloseClick() {
        onOutput(VideoPlayerComponent.Output.CloseRequested)
    }
}
