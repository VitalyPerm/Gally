@file:OptIn(ExperimentalMaterial3Api::class)

package ru.kvf.core.widgets

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource

@Composable
fun DefaultContainer(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int? = null,
    titleString: String? = null,
    onTrashClick: (() -> Unit)? = null,
    isScrollDown: MutableState<Boolean>? = null,
    onReverseClick: () -> Unit,
    onGridCountClick: () -> Unit,
    gridCount: Int,
    content: @Composable ColumnScope.() -> Unit
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(state = topAppBarState)
    val scrollDown by remember {
        derivedStateOf { topAppBarState.collapsedFraction > 0f }
    }
    isScrollDown?.value = scrollDown
    val title = titleString ?: titleRes?.let { stringResource(it) } ?: ""
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
//        TopAppBar(
//            title = { Text(text = title, style = MaterialTheme.typography.titleLarge) },
//            actions = {
//                GridCountIcon(count = gridCount, onClick = onGridCountClick)
//                ReverseIcon(onReverseClick)
//                onTrashClick?.let { TrashIcon(it) }
//            },
//            colors = TopAppBarDefaults.topAppBarColors(
//                containerColor = MaterialTheme.colorScheme.inversePrimary
//            ),
//            scrollBehavior = scrollBehavior,
//        )
        content()
    }
}
