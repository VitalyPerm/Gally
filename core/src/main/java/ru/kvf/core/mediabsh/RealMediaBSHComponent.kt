package ru.kvf.core.mediabsh

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.utils.coroutineScope
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RealMediaBSHComponent(
    componentContext: ComponentContext,
    override val media: StateFlow<List<Media>>,
    private val onOutput: (MediaBSHComponent.Output) -> Unit,
) : ComponentContext by componentContext, MediaBSHComponent {

    private companion object {
        const val TITLE_HIDING_TIMEOUT = 5000L
        const val TITLE_TIME_FORMAT = "dd.MM.yyyy HH:mm"
    }

    private val componentScope = lifecycle.coroutineScope()

    override val index = MutableStateFlow(0)

    private var titleHidingJob: Job? = null
    private val titleTimeFormat = SimpleDateFormat(TITLE_TIME_FORMAT, Locale.getDefault())

    override val title: StateFlow<String> = combine(media, index) { all, page ->
        titleTimeFormat.format(Date(all[page].timeStamp))
    }.stateIn(componentScope, SharingStarted.WhileSubscribed(5000), "")

    override val optionsVisible = MutableStateFlow(false)

    override fun onShareClick() {
    }

    override fun onTap() {
    }

    override fun onTrashClick() {
    }

    override fun onDismissRequest() {
        onOutput(MediaBSHComponent.Output.DismissRequested)
    }

    override fun setup(startIndex: Int) {
        index.update { startIndex }
    }
}
