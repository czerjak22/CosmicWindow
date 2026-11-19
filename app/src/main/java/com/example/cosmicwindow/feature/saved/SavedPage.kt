package com.example.cosmicwindow.feature.saved

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.cosmicwindow.R
import com.example.cosmicwindow.core.designsystem.components.AppHeader
import com.example.cosmicwindow.core.designsystem.components.CircleIconButton
import com.example.cosmicwindow.core.designsystem.components.CollapsingColumn
import com.example.cosmicwindow.core.designsystem.components.ErrorBox
import com.example.cosmicwindow.core.designsystem.theme.RadiusSize
import com.example.cosmicwindow.core.designsystem.theme.Spacing
import com.example.cosmicwindow.model.SavedItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withTimeoutOrNull
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedPage(viewModel: SavedViewModel = hiltViewModel(), onItemClicked: (SavedItem) -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val lifecycleOwner = LocalLifecycleOwner.current
    val snackbarHostState = remember { SnackbarHostState() }
    DisposableEffect(lifecycleOwner) {
        val observer =
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) viewModel.loadItems()
            }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                SavedEvent.ShowUndoDelete -> {
                    val result =
                        withTimeoutOrNull(SavedViewModel.UNDO_TIMEOUT) {
                            snackbarHostState.showSnackbar(
                                message = "Item removed",
                                actionLabel = "Undo",
                                duration = SnackbarDuration.Indefinite,
                            )
                        }

                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.undoDelete()
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehaviour.nestedScrollConnection),
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    actionColor = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(RadiusSize.r16),
                )
            }
        },
    ) { innerPadding ->

        if (uiState.error != null) {
            Box(
                modifier =
                    Modifier
                        .padding(innerPadding)
                        .padding(Spacing.s16)
                        .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                ErrorBox(text = uiState.error!!, onClick = viewModel::onErrorRetry)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CollapsingColumn(scrollBehavior = scrollBehaviour) {
                AppHeader(
                    leftIcon = R.drawable.earth_header_icon,
                    onClick = {},
                    leftIconSize = Spacing.s20,
                    rightIconComposable = {},
                    mainTitle = "Saved",
                    secondaryTitle = "COSMIC WINDOW",
                )
                OfflineStatsCard(
                    Modifier.padding(horizontal = Spacing.s16),
                    savedItemNumber = uiState.items.size,
                    downloadsSize = uiState.folderSize,
                )
                Spacer(Modifier.height(Spacing.s4))
            }
            SavedCardContainer(
                modifier = Modifier.padding(horizontal = Spacing.s16),
                savedItems = uiState.items,
                onSavedItemRemove = { viewModel.onDeleteClicked(it) },
                onSavedItemOpen = onItemClicked,
            )
        }
    }
}

@Composable
fun OfflineStatsCard(
    modifier: Modifier = Modifier,
    savedItemNumber: Int = 6,
    downloadsSize: Float = 142f,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.secondary,
        shape = RoundedCornerShape(RadiusSize.r12),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = Spacing.s16, vertical = Spacing.s16),
            horizontalArrangement = Arrangement.spacedBy(Spacing.s8),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(R.drawable.download_card_icon),
                contentDescription = null,
            )
            Column {
                Text(
                    "Offline Ready",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.W600,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    "$savedItemNumber discoveries synced to device",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            Spacer(Modifier.weight(1f))
            Surface(
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(RadiusSize.r9999),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = Spacing.s10, vertical = Spacing.s4),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.s4),
                ) {
                    Box(
                        modifier =
                            Modifier
                                .size(Spacing.s6)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onPrimaryContainer),
                    )
                    Text(
                        "${"%.1f".format(downloadsSize)} MB",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.W500,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
        }
    }
}

@Composable
fun SavedCardContainer(
    modifier: Modifier = Modifier,
    savedItems: List<SavedItem>,
    onSavedItemRemove: (SavedItem) -> Unit,
    onSavedItemOpen: (SavedItem) -> Unit,
) {
    val visibleItems = remember(savedItems) { savedItems.filter { it.apod.isSaved } }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(top = Spacing.s12, bottom = Spacing.s16),
        verticalArrangement = Arrangement.spacedBy(Spacing.s16),
    ) {
        items(
            items = visibleItems,
            key = { it.id },
        ) {
            if (it.apod.isSaved) {
                SavedCard(
                    modifier =
                        Modifier.animateItem(
                            fadeInSpec = tween(300),
                            fadeOutSpec = tween(300),
                            placementSpec =
                                spring(
                                    stiffness = Spring.StiffnessMediumLow,
                                    visibilityThreshold = IntOffset.VisibilityThreshold,
                                ),
                        ),
                    savedItem = it,
                    onBookmarkPressed = { onSavedItemRemove(it) },
                    onClickOpen = { onSavedItemOpen(it) },
                )
            }
        }
    }
}

@Composable
fun SavedCard(
    modifier: Modifier = Modifier,
    savedItem: SavedItem,
    onBookmarkPressed: () -> Unit,
    onClickOpen: () -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(RadiusSize.r12),
    ) {
        Column {
            Box {
                AsyncImage(
                    modifier = Modifier.aspectRatio(358f / 176f),
                    model = savedItem.imagePath,
                    contentDescription = savedItem.apod.title,
                    contentScale = ContentScale.Crop,
                )
                Box(
                    Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors =
                                    listOf(
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                                        MaterialTheme.colorScheme.surfaceVariant,
                                    ),
                            ),
                        ),
                )
                Box(contentAlignment = Alignment.TopStart) {
                    Row(
                        Modifier
                            .padding(Spacing.s8)
                            .fillMaxWidth(),
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f),
                            shape = RoundedCornerShape(RadiusSize.r9999),
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = Spacing.s12, vertical = Spacing.s6),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(Spacing.s2),
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.apod_icon),
                                    contentDescription = null,
                                )
                                Text(
                                    "APOD Story",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.W500,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                )
                            }
                        }

                        Spacer(Modifier.weight(1f))
                        CircleIconButton(
                            icon = R.drawable.bookmark_filled_icon,
                            description = null,
                            onClick = onBookmarkPressed,
                            backgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
                            foregroundColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }
                }
            }
            Column(
                modifier =
                    Modifier
                        .padding(Spacing.s16)
                        .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Spacing.s8),
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                ) {
                    Text(
                        AnnotatedString.fromHtml(savedItem.apod.credit),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.W500,
                        color = MaterialTheme.colorScheme.onSecondary,
                        maxLines = 1,
                        modifier = Modifier.weight(1f),
                    )

                    Text(
                        savedItem.apod.date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.W500,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                }
                Text(
                    savedItem.apod.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    AnnotatedString.fromHtml(savedItem.apod.alt),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.s2),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.s2),
                    ) {
                        Image(
                            painter = painterResource(R.drawable.checkmark_icon),
                            contentDescription = null,
                        )
                        Text(
                            "Saved offline · " + savedItem.savedDate.format(DateTimeFormatter.ofPattern("MMM yy")),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.W500,
                            color = MaterialTheme.colorScheme.onSecondary,
                        )
                    }
                    Spacer(Modifier.weight(1f))

                    Surface(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(RadiusSize.r9999),
                        onClick = onClickOpen,
                    ) {
                        Row(
                            modifier = Modifier.padding(Spacing.s10),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Spacing.s2),
                        ) {
                            Text(
                                "Read Story",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.W500,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )

                            Image(
                                painter = painterResource(R.drawable.to_story_icon),
                                contentDescription = null,
                            )
                        }
                    }
                }
            }
        }
    }
}
