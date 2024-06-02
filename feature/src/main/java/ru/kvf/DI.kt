package ru.kvf

import android.content.Context
import android.content.res.Resources
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.get
import org.koin.dsl.module
import ru.kvf.core.ComponentFactory
import ru.kvf.core.utils.MediaList
import ru.kvf.feature.favorite.FavoriteComponent
import ru.kvf.feature.favorite.RealFavoriteComponent
import ru.kvf.feature.favorite.folders.FavoriteFoldersComponent
import ru.kvf.feature.favorite.folders.RealFavoriteFoldersComponent
import ru.kvf.feature.favorite.media.FavoriteMediaComponent
import ru.kvf.feature.favorite.media.RealFavoriteMediaComponent
import ru.kvf.feature.folders.details.FolderDetailsComponent
import ru.kvf.feature.folders.details.RealFolderDetailsComponent
import ru.kvf.feature.folders.list.FoldersListComponent
import ru.kvf.feature.folders.list.RealFoldersListComponent
import ru.kvf.feature.media.data.GetSortedMediaUseCaseImpl
import ru.kvf.feature.media.data.MediaFilterUseCaseImpl
import ru.kvf.feature.media.domain.GetSortedMediaUseCase
import ru.kvf.feature.media.domain.MediaFilterUseCase
import ru.kvf.feature.media.ui.MediaListComponent
import ru.kvf.feature.media.ui.RealMediaListComponent
import ru.kvf.feature.mediabsh.MediaBSHComponent
import ru.kvf.feature.mediabsh.RealMediaBSHComponent
import ru.kvf.feature.mediadetails.MediaDetailsComponent
import ru.kvf.feature.mediadetails.RealMediaDetailsComponent
import ru.kvf.feature.settings.RealSettingsListComponent
import ru.kvf.feature.settings.SettingsListComponent
import ru.kvf.feature.trash.RealTrashComponent
import ru.kvf.feature.trash.TrashComponent

val featureModule = module {
    single<Resources> { get<Context>().resources }
    single<GetSortedMediaUseCase> { GetSortedMediaUseCaseImpl(get(), get(), get()) }
    single<MediaFilterUseCase> { MediaFilterUseCaseImpl() }
}

fun ComponentFactory.createMediaListComponent(
    componentContext: ComponentContext,
    output: (MediaListComponent.Output) -> Unit
): MediaListComponent = RealMediaListComponent(
    componentContext,
    output,
    get(),
    get(),
    get(),
    get(),
    get(),
    get(),
    get(),
    get(),
    get(),
    get()
)

fun ComponentFactory.createFolderDetailsComponent(
    componentContext: ComponentContext,
    folderName: String,
): FolderDetailsComponent = RealFolderDetailsComponent(
    componentContext,
    folderName,
    get(),
    get(),
    get(),
    get(),
    get(),
    get(),
    get(),
    get(),
    get()
)

fun ComponentFactory.createFavoriteComponent(
    componentContext: ComponentContext,
    output: (FavoriteComponent.Output) -> Unit
): FavoriteComponent = RealFavoriteComponent(
    componentContext,
    get(),
    output,
    get()
)

fun ComponentFactory.createFavoriteFoldersComponent(
    componentContext: ComponentContext,
    output: (FavoriteFoldersComponent.Output) -> Unit
): FavoriteFoldersComponent = RealFavoriteFoldersComponent(
    componentContext,
    output,
    get(),
    get()
)

fun ComponentFactory.createFavoriteMediaComponent(
    componentContext: ComponentContext,
): FavoriteMediaComponent = RealFavoriteMediaComponent(
    componentContext,
    get(),
    get(),
    get()
)

fun ComponentFactory.createFoldersListComponent(
    componentContext: ComponentContext,
    output: (FoldersListComponent.Output) -> Unit
): FoldersListComponent = RealFoldersListComponent(
    componentContext,
    output,
    get(),
    get(),
    get(),
    get()
)

fun ComponentFactory.createSettingsListComponent(
    componentContext: ComponentContext
): SettingsListComponent = RealSettingsListComponent(
    componentContext,
    get(),
    get(),
    get(),
    get()
)

fun ComponentFactory.createMediaBSHComponent(
    componentContext: ComponentContext,
    media: StateFlow<MediaList>,
    isTrash: Boolean = false
): MediaBSHComponent = RealMediaBSHComponent(
    componentContext,
    isTrash = isTrash,
    media,
    get(),
    get(),
    get(),
    get(),
    get(),
)

fun ComponentFactory.createTrashComponent(
    componentContext: ComponentContext
): TrashComponent = RealTrashComponent(
    componentContext,
    get(),
    get(),
    get(),
    get(),
    get(),
    get(),
    get()
)

fun ComponentFactory.createMediaDetailsComponent(
    componentContext: ComponentContext,
    type: MediaDetailsComponent.Type,
    mediaId: Long
): MediaDetailsComponent = RealMediaDetailsComponent(
    componentContext,
    type,
    mediaId,
    get(),
    get(),
    get(),
    get(),
    get(),
    get(),
)
