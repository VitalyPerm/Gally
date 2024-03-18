@file:OptIn(ExperimentalFoundationApi::class)

package ru.kvf.feature.favorite

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.kvf.feature.favorite.folders.FavoriteFoldersUi
import ru.kvf.feature.favorite.media.FavoriteMediaUi
import ru.kvf.core.R as CoreR

@Composable
fun FavoriteUi(component: FavoriteComponent) {
    val pagerState = rememberPagerState { Pages.entries.size }
    val gridCellsCount by component.gridCellsCount.collectAsState()
    val isReversed by component.isReversed.collectAsState()

    Column(
        modifier = Modifier
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        TabRow(pagerState)
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalPager(state = pagerState) {
            when (Pages.fromIndex(it)) {
                Pages.Media -> FavoriteMediaUi(
                    component = component.favoriteMediaComponent,
                    gridCellsCount = gridCellsCount,
                    isReversed = isReversed
                )

                Pages.Folders -> FavoriteFoldersUi(
                    component = component.favoriteFoldersComponent,
                    cellsCount = gridCellsCount,
                    isReversed = isReversed
                )
            }
        }
    }
}

@Composable
private fun TabRow(pagerState: PagerState) {
    val currentOffset by remember {
        derivedStateOf { pagerState.getOffsetFractionForPage(0).takeIf { it != 0f } ?: 0.0001f }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        val ovalColor = MaterialTheme.colorScheme.surfaceVariant
        val ld = LocalDensity.current
        Row(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.extraLarge)
                .drawWithContent {
                    drawRoundRect(
                        color = ovalColor,
                        size = Size(
                            width = size.width
                                .div(2)
                                .minus(12 * ld.density),
                            height = size.height.minus(12 * ld.density)
                        ),
                        cornerRadius = CornerRadius(16f * ld.density, 16f * ld.density),
                        topLeft = Offset(
                            x = 6 * ld.density + (size.width.div(2)).times(currentOffset),
                            y = 6 * ld.density
                        )
                    )
                    drawContent()
                },
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Pages.entries.forEach {
                TabRowItem(page = it)
            }
        }
    }
}

@Composable
private fun RowScope.TabRowItem(page: Pages) {
    Text(
        text = stringResource(page.getString()),
        style = MaterialTheme.typography.labelLarge,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .padding(vertical = 6.dp)
            .weight(1f)
    )
}

private enum class Pages {
    Media, Folders;

    fun getString() = when (this) {
        Media -> CoreR.string.media
        Folders -> CoreR.string.folders
    }

    companion object {
        fun fromIndex(index: Int) = if (index == 0) Media else Folders
    }
}
