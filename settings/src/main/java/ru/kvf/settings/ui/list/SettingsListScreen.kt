package ru.kvf.settings.ui.list

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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import ru.kvf.core.domain.entities.ThemeType
import ru.kvf.settings.R
import ru.kvf.settings.ui.list.theme.ChooseThemeBSH

@Composable
fun SettingsListScreen(
    vm: SettingsListViewModel = koinViewModel()
) {
    val state by vm.collectAsState()

    Content(
        state = state,
        settings = state.settings.toImmutableList(),
        onSettingChanged = vm::onSettingChanged,
        onThemeTypeSelected = vm::onThemeTypeSelected
    )
}

@Composable
private fun Content(
    state: SettingsListState,
    settings: ImmutableList<Pair<Setting, Boolean>>,
    onSettingChanged: (Setting, Boolean) -> Unit,
    onThemeTypeSelected: (ThemeType) -> Unit
) {
    val chooseThemeBSHVisible = remember { mutableStateOf(false) }
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(BorderStroke(1.dp, Color.Blue), MaterialTheme.shapes.medium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.theme),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(16.dp)
            )

            TextButton(onClick = { chooseThemeBSHVisible.value = true }) {
                Text(
                    text = stringResource(state.theme.getRes()),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
        }
    }

    ChooseThemeBSH(
        visible = chooseThemeBSHVisible,
        onTypeSelected = onThemeTypeSelected,
        currentTheme = state.theme
    )
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
