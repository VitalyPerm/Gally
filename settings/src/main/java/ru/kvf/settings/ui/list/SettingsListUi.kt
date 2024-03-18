package ru.kvf.settings.ui.list

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.kvf.core.domain.entities.ThemeType
import ru.kvf.core.widgets.DefaultContainer
import ru.kvf.settings.R
import ru.kvf.settings.ui.list.theme.ChooseThemeBSH
import java.util.Calendar

@Composable
fun SettingsListUi(
    component: SettingsListComponent,
) {
    val edgeToEdgEnable by component.edgeToEdgeEnable.collectAsState()
    val sortBy by component.sortBy.collectAsState()
    val theme by component.theme.collectAsState()
    Content(
        edgeToEdgEnable = edgeToEdgEnable,
        sortBy = sortBy,
        theme = theme,
        onEdgeToEdgeChanged = component::onEdgeToEdgeChanged,
        onThemeTypeSelected = component::onThemeChanged,
        onSortByChanged = component::onSortByChanged
    )
}

@Composable
private fun Content(
    edgeToEdgEnable: Boolean,
    sortBy: Int,
    theme: ThemeType,
    onEdgeToEdgeChanged: (Boolean) -> Unit,
    onThemeTypeSelected: (ThemeType) -> Unit,
    onSortByChanged: (Int) -> Unit
) {
    val chooseThemeBSHVisible = remember { mutableStateOf(false) }

    DefaultContainer(titleRes = R.string.settings) {
        EdgeToEdge(
            enable = edgeToEdgEnable,
            onCheckedChange = onEdgeToEdgeChanged
        )
        Theme(
            currentTheme = theme,
            onCurrentThemeClick = { chooseThemeBSHVisible.value = true }
        )

        SortBy(
            currentSortBy = sortBy,
            onSortByChanged = onSortByChanged
        )
        ChooseThemeBSH(
            visible = chooseThemeBSHVisible,
            onTypeSelected = onThemeTypeSelected,
            currentTheme = theme
        )
    }
}

@Composable
private fun SortBy(
    currentSortBy: Int,
    onSortByChanged: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(BorderStroke(1.dp, Color.Blue), MaterialTheme.shapes.medium)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.sort_by),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .border(
                    BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
                    MaterialTheme.shapes.extraLarge
                )
        ) {
            val lineColor = MaterialTheme.colorScheme.onSurface
            val bgDayColor by animateColorAsState(
                targetValue = if (currentSortBy == Calendar.DAY_OF_YEAR) {
                    MaterialTheme.colorScheme.inversePrimary
                } else {
                    MaterialTheme.colorScheme.onPrimary
                },
                label = ""
            )
            val textDayColor by animateColorAsState(
                targetValue = if (currentSortBy == Calendar.DAY_OF_YEAR) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                label = ""
            )
            Text(
                text = stringResource(R.string.sort_by_days),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = textDayColor,
                modifier = Modifier
                    .drawBehind {
                        drawLine(
                            color = lineColor,
                            strokeWidth = 3f,
                            start = Offset(size.width, 0f),
                            end = Offset(size.width, size.height)
                        )
                    }
                    .clip(RoundedCornerShape(topStart = 28.dp, bottomStart = 28.dp))
                    .background(bgDayColor)
                    .clickable { onSortByChanged(Calendar.DAY_OF_YEAR) }
                    .weight(1f)
                    .padding(8.dp)
            )
            val bgMonthColor by animateColorAsState(
                targetValue = if (currentSortBy == Calendar.MONTH) {
                    MaterialTheme.colorScheme.inversePrimary
                } else {
                    MaterialTheme.colorScheme.onPrimary
                },
                label = ""
            )
            val textMonthColor by animateColorAsState(
                targetValue = if (currentSortBy == Calendar.MONTH) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                label = ""
            )
            Text(
                text = stringResource(R.string.sort_by_moth),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = textMonthColor,
                modifier = Modifier
                    .clip(RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp))
                    .background(bgMonthColor)
                    .clickable { onSortByChanged(Calendar.MONTH) }
                    .weight(1f)
                    .padding(8.dp)

            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
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
private fun EdgeToEdge(enable: Boolean, onCheckedChange: (Boolean) -> Unit) {
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
