package ru.kvf.settings.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import ru.kvf.core.domain.entities.Setting
import ru.kvf.core.utils.log

@Composable
fun SettingsListScreen(
    vm: SettingsListViewModel = koinViewModel()
) {
    val state by vm.collectAsState()

    Content(
        settings = state.settings.toImmutableList(),
        onSettingChanged = vm::onSettingChanged
    )
}

@Composable
private fun Content(
    settings: ImmutableList<Pair<Setting, Boolean>>,
    onSettingChanged: (Setting, Boolean) -> Unit
) {
    log("settings = $settings")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding()
    ) {
        settings.forEach { (setting, enable) ->
            SettingItem(
                title = stringResource(setting.name),
                enable = enable,
                onCheckedChange = { checked -> onSettingChanged(setting, checked) }
            )
        }
    }
}

@Composable
fun SettingItem(title: String, enable: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(BorderStroke(1.dp, Color.Blue), MaterialTheme.shapes.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            modifier = Modifier
                .padding(16.dp)
        )

        Switch(
            checked = enable,
            onCheckedChange = onCheckedChange,
            modifier = Modifier
                .padding(16.dp)
        )
    }
}
