package ru.kvf.design

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.kvf.photos.R

private val pages = listOf(R.string.colors, R.string.typo, R.string.shape)

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DesignUi(
    navBarPadding: Dp
) {
    val pagerState = rememberPagerState {
        pages.size
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(2.dp)
            .padding(bottom = navBarPadding)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(pages[pagerState.currentPage]),
                    style = MaterialTheme.typography.titleLarge
                )
            },
        )
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
        ) { page ->
            when (page) {
                0 -> Colors()
                1 -> Typo()
                else -> Shapes()
            }
        }
    }
}

@Composable
private fun ColumnScope.Shapes() {
    val shapes = listOf(
        MaterialTheme.shapes.extraSmall to "extraSmall",
        MaterialTheme.shapes.small to "small",
        MaterialTheme.shapes.medium to "medium",
        MaterialTheme.shapes.large to "large",
        MaterialTheme.shapes.extraLarge to "extraLarge",
    )

    Column(
        modifier = Modifier
            .weight(1f)
    ) {
        shapes.forEach {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.dp)
                    .background(Color.Blue.copy(alpha = 0.3f), it.first),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = it.second,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.Typo() {
    val typos = listOf(
        MaterialTheme.typography.displayLarge to "displayLarge",
        MaterialTheme.typography.displayMedium to "displayMedium",
        MaterialTheme.typography.displaySmall to "displaySmall",
        MaterialTheme.typography.headlineLarge to "headlineLarge",
        MaterialTheme.typography.headlineSmall to "headlineSmall",
        MaterialTheme.typography.titleLarge to "titleLarge",
        MaterialTheme.typography.titleMedium to "titleMedium",
        MaterialTheme.typography.titleSmall to "titleSmall",
        MaterialTheme.typography.bodyLarge to "bodyLarge",
        MaterialTheme.typography.bodyMedium to "bodyMedium",
        MaterialTheme.typography.bodySmall to "bodySmall",
        MaterialTheme.typography.labelLarge to "labelLarge",
        MaterialTheme.typography.labelMedium to "labelMedium",
        MaterialTheme.typography.labelSmall to "labelSmall",
    )

    Column(
        modifier = Modifier
            .weight(1f)
    ) {
        typos.forEach {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.dp)
                    .border(BorderStroke(1.dp, Color.Black)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = it.second,
                    style = it.first
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.Colors() {
    val colors = listOf(
        MaterialTheme.colorScheme.primary to "primary",
        MaterialTheme.colorScheme.onPrimary to "onPrimary",
        MaterialTheme.colorScheme.primaryContainer to "primaryCon",
        MaterialTheme.colorScheme.inversePrimary to "invPrimary",
        MaterialTheme.colorScheme.secondary to "secondary",
        MaterialTheme.colorScheme.onSecondary to "onSecondary",
        MaterialTheme.colorScheme.secondaryContainer to "secondaryCon",
        MaterialTheme.colorScheme.onSecondaryContainer to "onSecondaryCon",
        MaterialTheme.colorScheme.tertiary to "tertiary",
        MaterialTheme.colorScheme.onTertiary to "onTertiary",
        MaterialTheme.colorScheme.tertiaryContainer to "tertiaryCon",
        MaterialTheme.colorScheme.onTertiaryContainer to "onTertiaryCon",
        MaterialTheme.colorScheme.background to "background",
        MaterialTheme.colorScheme.onBackground to "onBackground",
        MaterialTheme.colorScheme.surface to "surface",
        MaterialTheme.colorScheme.onSurface to "onSurface",
        MaterialTheme.colorScheme.surfaceVariant to "surfaceVariant",
        MaterialTheme.colorScheme.onSurfaceVariant to "onSurfaceVariant",
        MaterialTheme.colorScheme.surfaceTint to "surfaceTint",
        MaterialTheme.colorScheme.inverseSurface to "inverseSurface",
        MaterialTheme.colorScheme.error to "error",
        MaterialTheme.colorScheme.onError to "onError",
        MaterialTheme.colorScheme.errorContainer to "errorContainer",
        MaterialTheme.colorScheme.onErrorContainer to "onErrorContainer",
        MaterialTheme.colorScheme.outline to "outline",
        MaterialTheme.colorScheme.outlineVariant to "outlineVariant",
        MaterialTheme.colorScheme.scrim to "scrim",
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .weight(1f)
    ) {
        items(colors) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
                    .border(BorderStroke(1.dp, color = Color.Black))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .background(it.first)
                )
                Text(
                    text = it.second,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
