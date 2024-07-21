package ru.kvf.gally.root

import android.net.Uri
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.ThemeType
import ru.kvf.feature.folders.details.FolderDetailsComponent
import ru.kvf.feature.media.ui.MediaListComponent
import ru.kvf.feature.mediadetails.MediaDetailsComponent
import ru.kvf.feature.trash.TrashComponent
import ru.kvf.feature.video.VideoPlayerComponent
import ru.kvf.gally.home.HomeComponent

interface RootComponent : BackHandlerOwner {

    val theme: StateFlow<ThemeType>
    val childStack: Value<ChildStack<*, Child>>
    val sideEffect: Flow<SideEffect>

    fun onBackClicked()

    sealed interface Child {
        class Home(val component: HomeComponent) : Child
        class FolderMediaList(val component: MediaListComponent) : Child
        class Trash(val component: TrashComponent) : Child
        class FolderDetails(val component: FolderDetailsComponent) : Child
        class MediaDetails(val component: MediaDetailsComponent) : Child
        class VideoPlayer(val component: VideoPlayerComponent) : Child
    }

    sealed interface SideEffect {
        data object Vibrate : SideEffect
        data class ShareMedia(val mediaList: List<Media>) : SideEffect
        data class TrashMedia(val uris: Set<Uri>, val toTrash: Boolean) : SideEffect
        data class DeleteMedia(val uris: Set<Uri>) : SideEffect
    }
}
