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
import androidx.compose.material3.Button
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
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
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
        onEdgeToEdgeChanged = vm::onEdgeToEdgeChanged,
        onThemeTypeSelected = vm::onThemeChanged,
        onSortByChanged = vm::onSortByChanged
    )
}

@Composable
private fun Content(
    state: SettingsListState,
    onEdgeToEdgeChanged: (Boolean) -> Unit,
    onThemeTypeSelected: (ThemeType) -> Unit,
    onSortByChanged: () -> Unit
) {
    val chooseThemeBSHVisible = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding()
    ) {
        EdgeToEdge(
            enable = state.edgeToEdge,
            onCheckedChange = onEdgeToEdgeChanged
        )
        Theme(
            currentTheme = state.theme,
            onCurrentThemeClick = { chooseThemeBSHVisible.value = true }
        )

        Button(onClick = onSortByChanged) {
            Text(text = "Test")
        }
    }

    ChooseThemeBSH(
        visible = chooseThemeBSHVisible,
        onTypeSelected = onThemeTypeSelected,
        currentTheme = state.theme
    )
}

@Composable
private fun Theme(
    onCurrentThemeClick: () -> Unit,
    currentTheme: ThemeType
) {
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

        TextButton(onClick = onCurrentThemeClick) {
            Text(
                text = stringResource(currentTheme.getRes()),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun EdgeToEdge(enable: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(BorderStroke(1.dp, Color.Blue), MaterialTheme.shapes.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.edge_to_edge),
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
