package com.example.cosmicwindow.feature.exploredetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.cosmicwindow.R
import com.example.cosmicwindow.core.designsystem.components.CircleIconButton
import com.example.cosmicwindow.core.designsystem.components.ErrorBox
import com.example.cosmicwindow.core.designsystem.components.LottieLoader
import com.example.cosmicwindow.core.designsystem.theme.RadiusSize
import com.example.cosmicwindow.core.designsystem.theme.Spacing
import com.example.cosmicwindow.model.SearchResultItem
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Composable
fun ExploreDetailScreen(viewModel: ExploreDetailViewModel = hiltViewModel(), onClickBack: () -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold { innerPadding ->

        when {
            uiState.error != null -> {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    ErrorBox(text = uiState.error!!, onClick = viewModel::onErrorRetry)
                }
                return@Scaffold
            }

            uiState.isLoading -> {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    LottieLoader()
                }
                return@Scaffold
            }
        }

        ImagePage(
            modifier = Modifier.padding(innerPadding),
            onBackPressed = onClickBack,
            searchResult = uiState.searchItem!!,
            onPictureLoaded = { viewModel.pictureLoaded() },
            onErrorAction = viewModel::imageLoadError,
        )
        if (!uiState.pictureLoaded) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center,
            ) { LottieLoader() }
            return@Scaffold
        }
    }
}

@Composable
fun Details(
    searchResult: SearchResultItem,
    modifier: Modifier = Modifier,
    previewLines: Int = 3,
    onCollapsedHeight: (Dp) -> Unit = {},
) {
    val density = LocalDensity.current
    var titlePx by remember { mutableIntStateOf(0) }
    var previewPx by remember { mutableIntStateOf(0) }

    LaunchedEffect(titlePx, previewPx) {
        onCollapsedHeight(with(density) { (titlePx + previewPx).toDp() } + Spacing.s8)
    }

    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Spacing.s8)) {
        Text(
            searchResult.title,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            onTextLayout = { titlePx = it.size.height },
        )
        Text(
            AnnotatedString.fromHtml(searchResult.description),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            onTextLayout = {
                val lines = minOf(previewLines, it.lineCount)
                previewPx = if (lines > 0) it.getLineBottom(lines - 1).roundToInt() else 0
            },
        )
    }
}

@Composable
private fun DescriptionSheet(
    searchResult: SearchResultItem,
    sheetColor: Color,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    var collapsed by remember { mutableStateOf(0.dp) }

    BoxWithConstraints(modifier) {
        val fadeHeight = maxHeight * 0.15f
        val topPadding = fadeHeight * 0.2f
        val imageGap = (maxHeight - topPadding - collapsed - Spacing.s16).coerceAtLeast(0.dp)
        val fadePx = with(density) { fadeHeight.toPx() }

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(Modifier.height(imageGap))

            Column(
                Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, sheetColor.copy(alpha = 0.85f)),
                            startY = 0f,
                            endY = fadePx,
                        ),
                    ).padding(start = Spacing.s16, end = Spacing.s16, top = topPadding, bottom = Spacing.s16),
            ) {
                Details(searchResult = searchResult, onCollapsedHeight = { collapsed = it })
            }
        }
    }
}

@Composable
fun DetailKeywords(
    searchResult: SearchResultItem,
    sheetColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(sheetColor.copy(alpha = 0.85f), sheetColor)))
            .navigationBarsPadding()
            .padding(Spacing.s16),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(RadiusSize.r12),
                ).padding(Spacing.s16),
            horizontalArrangement = Arrangement.spacedBy(Spacing.s8, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ColumnElement(
                title = stringResource(R.string.observation_date),
                content = searchResult.dateCreated.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
            )
            if (searchResult.center != null) {
                ColumnElement(
                    title = stringResource(R.string.research_center_label),
                    content = searchResult.center,
                )
            }
            if (searchResult.photographer != null) {
                ColumnElement(
                    title = stringResource(R.string.photographer_label),
                    content = searchResult.photographer,
                )
            }
        }
    }
}

@Composable
fun ColumnElement(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.s8),
    ) {
        Text(
            title,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.W500,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            AnnotatedString.fromHtml(content),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
fun ImagePage(
    searchResult: SearchResultItem,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    onPictureLoaded: () -> Unit,
    onErrorAction: (errorMessage: String) -> Unit,
) {
    val sheetColor = MaterialTheme.colorScheme.onSecondaryContainer

    Box(modifier = modifier.fillMaxSize()) {
        AsyncImage(
            model = searchResult.originalUrl ?: searchResult.thumbnailUrl,
            contentDescription = searchResult.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            onSuccess = {
                onPictureLoaded()
            },
            onError = { err ->
                onErrorAction(err.toString())
            },
        )

        Column(Modifier.fillMaxSize()) {
            DescriptionSheet(
                searchResult,
                sheetColor,
                Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            )
            DetailKeywords(searchResult, sheetColor)
        }

        CircleIconButton(
            icon = R.drawable.exit_icon,
            description = stringResource(R.string.back_button_description),
            onClick = onBackPressed,
            backgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
            modifier =
                Modifier
                    .align(Alignment.TopStart)
                    .padding(Spacing.s16),
            iconSize = Spacing.s14,
        )
    }
}
