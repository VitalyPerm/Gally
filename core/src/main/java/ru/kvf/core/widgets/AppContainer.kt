@file:OptIn(ExperimentalMaterial3Api::class)

package ru.kvf.core.widgets

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiProtectedSetup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun DefaultContainer(
    @StringRes titleRes: Int? = null,
    titleString: String? = null,
    reverseActionEnable: Boolean = false,
    onReverseClick: () -> Unit = {},
    gridCountActionEnable: Boolean = false,
    onGridCountClick: () -> Unit = {},
    gridCount: Int = 1,
    content: @Composable ColumnScope.() -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val title = titleString ?: titleRes?.let { stringResource(it) } ?: ""
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        TopAppBar(
            title = { Text(text = title, style = MaterialTheme.typography.titleLarge) },
            actions = {
                if (gridCountActionEnable) GridCountIcon(count = gridCount, onClick = onGridCountClick)
                if (reverseActionEnable) ReverseIcon(onReverseClick)
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.inversePrimary
            ),
            scrollBehavior = scrollBehavior,
        )
        content()
    }
}

@Composable
private fun ReverseIcon(
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Filled.WifiProtectedSetup,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun GridCountIcon(
    count: Int,
    onClick: () -> Unit
) {
    TextButton(onClick = onClick) {
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}
