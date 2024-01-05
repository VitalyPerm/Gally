package ru.kvf.settings.ui.list.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.kvf.core.domain.entities.ThemeType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseThemeBSH(
    visible: MutableState<Boolean>,
    currentTheme: ThemeType,
    onTypeSelected: (ThemeType) -> Unit
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    if (visible.value) {
        ModalBottomSheet(
            onDismissRequest = { visible.value = false },
            sheetState = sheetState,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ThemeType.entries.forEach { theme ->
                    TextButton(onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            visible.value = false
                            onTypeSelected(theme)
                        }
                    }) {
                        val backgroundModifier = if (theme == currentTheme) {
                            Modifier.background(
                                MaterialTheme.colorScheme.inversePrimary,
                                MaterialTheme.shapes.extraLarge
                            )
                        } else {
                            Modifier
                        }
                        Text(
                            text = stringResource(theme.getRes()),
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .then(backgroundModifier)
                                .padding(16.dp)

                        )
                    }
                }
            }
        }
    }
}
