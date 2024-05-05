package ru.kvf.feature.trash

import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.utils.MediaList

interface TrashComponent {

    val media: StateFlow<MediaList>

}