package ru.kvf.photos.ui.list.delete

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.size.Size
import kotlinx.coroutines.launch
import ru.kvf.core.domain.entities.Photo
import ru.kvf.core.widgets.PhotoItem
import ru.kvf.photos.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeletePhotoBSH(
    photo: Photo?,
    onDeleteClick: () -> Unit,
    onDismissClick: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (photo != null) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch { sheetState.hide() }.invokeOnCompletion { onDismissClick() }
            },
            sheetState = sheetState,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = stringResource(R.string.delete_photo_bsh_title),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(16.dp)

                )
                Spacer(modifier = Modifier.height(16.dp))

                PhotoItem(
                    model = photo.uri,
                    size = Size.ORIGINAL
                )

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
                            text = stringResource(R.string.delete_photo_bsh_btn_ok),
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }

                    Button(onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion { onDismissClick() }
                    }) {
                        Text(
                            text = stringResource(R.string.delete_photo_bsh_btn_no),
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
