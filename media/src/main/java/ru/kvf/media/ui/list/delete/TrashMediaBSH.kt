package ru.kvf.media.ui.list.delete

import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import ru.kvf.media.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrashMediaBSH(
    media: Set<Uri>,
    onDeleteClick: () -> Unit,
    onDismissClick: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (media.isNotEmpty()) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch { sheetState.hide() }.invokeOnCompletion { onDismissClick() }
            },
            sheetState = sheetState,
            windowInsets = WindowInsets.displayCutout
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.delete_media_bsh_title, media.size),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(16.dp)

                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    media.forEach { uri ->
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(4.dp)
                                .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
                                .clip(RoundedCornerShape(16.dp))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion { onDeleteClick() }
                    }) {
                        Text(
                            text = stringResource(R.string.delete_media_bsh_btn_ok),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Button(onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion { onDismissClick() }
                    }) {
                        Text(
                            text = stringResource(R.string.delete_media_bsh_btn_no),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
