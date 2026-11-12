package com.example.cosmicwindow.feature.explore

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldDecorator
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.palette.graphics.Palette
import coil3.compose.AsyncImage
import coil3.toBitmap
import com.example.cosmicwindow.R
import com.example.cosmicwindow.core.designsystem.components.AppHeader
import com.example.cosmicwindow.core.designsystem.components.ErrorBox
import com.example.cosmicwindow.core.designsystem.theme.RadiusSize
import com.example.cosmicwindow.core.designsystem.theme.Spacing
import com.example.cosmicwindow.model.Filter
import com.example.cosmicwindow.model.SearchResultItem
import java.time.format.DateTimeFormatter

@Composable
fun ExplorePage(viewModel: ExploreViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            AppHeader(
                leftIcon = R.drawable.earth_header_icon,
                onClick = {},
                leftIconSize = Spacing.s20,
                leftIconComposable = {
                    Image(
                        painterResource(R.drawable.profile_image),
                        contentDescription = null,
                    )
                },
                mainTitle = "Explore NASA",
                secondaryTitle = "IMAGE LIBRARY",
            )
        },
    ) { innerPadding ->

        if (uiState.error != null) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                ErrorBox(text = uiState.error!!, onClick = viewModel::onErrorRetry)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = Spacing.s16),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.s8),
        ) {
            SearchBar(
                modifier = Modifier.padding(horizontal = Spacing.s16),
                onQueryChanged = { text -> viewModel.onQueryChanged(text) },
                onSearch = { viewModel.newSearch() },
                onVoiceInputClicked = {})

            FilterBar(
                onFilterClick = viewModel::toggleFilter,
                filters = uiState.queryState.filters,
            )
            ImageGrid(
                Modifier.fillMaxSize(),
                uiState.searchItems,
                isLoadingMore = uiState.isLoading,
                onLoadMore = viewModel::loadMore
            )
        }
    }
}


@Composable
fun CardElement(
    modifier: Modifier = Modifier,
    searchItem: SearchResultItem,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(RadiusSize.r16)
    var isLoaded by remember(searchItem.nasaId) { mutableStateOf(false) }
    var accentColor by remember(searchItem.nasaId) { mutableStateOf<Color?>(null) }

    val alpha by animateFloatAsState(
        targetValue = if (isLoaded) 1f else 0f,
        animationSpec = tween(300),
        label = "cardAlpha",
    )

    Surface(
        modifier = modifier
            .graphicsLayer { this.alpha = alpha }
            .clip(shape)
            .clickable(onClick = onClick),
        shape = shape,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column {
            Box {
                AsyncImage(
                    model = searchItem.thumbnailUrl,
                    contentDescription = searchItem.title,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth,

                    onSuccess = { state ->
                        isLoaded = true
                        val bitmap = state.result.image.toBitmap()
                        Palette.from(bitmap).generate { palette ->
                            val swatch = palette?.vibrantSwatch
                                ?: palette?.mutedSwatch
                                ?: palette?.dominantSwatch
                            accentColor = swatch?.let { Color(it.rgb) }
                        }
                    },
                )

                Box(
                    Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                                )
                            )
                        )
                )

                Surface(
                    modifier = Modifier.padding(Spacing.s8),
                    shape = RoundedCornerShape(RadiusSize.r9999),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
                ) {
                    Text(
                        text = searchItem.nasaId,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = Spacing.s8, vertical = Spacing.s4)
                    )
                }
            }


            Column(
                Modifier.padding(horizontal = Spacing.s16, vertical = Spacing.s12),
                verticalArrangement = Arrangement.spacedBy(Spacing.s8)
            ) {
                Text(
                    text = searchItem.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = searchItem.dateCreated.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (searchItem.center != null) {
                        Text(
                            text = searchItem.center,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun LoadingRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            Spacing.s8,
            alignment = Alignment.CenterHorizontally
        )
    ) {
        CircularProgressIndicator()
        Text(
            "Loading more NASA archive images...",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

    }
}

@Composable
fun ImageGrid(
    modifier: Modifier = Modifier,
    searchResults: List<SearchResultItem>,
    isLoadingMore: Boolean,
    onLoadMore: () -> Unit
) {
    val gridState = rememberLazyStaggeredGridState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val info = gridState.layoutInfo
            val lastVisible = info.visibleItemsInfo.lastOrNull()?.index ?: return@derivedStateOf false
            lastVisible >= info.totalItemsCount - 4   // prefetch 4 items before the end
        }
    }

    LaunchedEffect(shouldLoadMore, searchResults.size) {
        if (shouldLoadMore) onLoadMore()
    }


    LazyVerticalStaggeredGrid(
        state = gridState,
        modifier = modifier,
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = Spacing.s8,
        horizontalArrangement = Arrangement.spacedBy(Spacing.s8),
    ) {
        items(
            items = searchResults,
            key = { it.nasaId }
        )
        { searchItem ->
            CardElement(onClick = {}, searchItem = searchItem)
        }

        if (isLoadingMore) {
            item(span = StaggeredGridItemSpan.FullLine) {
                LoadingRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Spacing.s16)
                )
            }
        }
        item(span = StaggeredGridItemSpan.FullLine) {
            Spacer(Modifier.height(Spacing.s16))
        }

    }
}

@Composable
fun FilterButton(filter: Filter, onClick: () -> Unit) {

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(RadiusSize.r9999),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (filter.isActive) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else MaterialTheme.colorScheme.secondary,
            contentColor = if (filter.isActive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.s8)
        )
        {
            Icon(
                painter = painterResource(filter.icon),
                contentDescription = "filterButton",
                modifier = Modifier.size(Spacing.s14),
            )
            Text(
                filter.text,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}


@Composable
fun FilterBar(modifier: Modifier = Modifier, filters: List<Filter>, onFilterClick: (Int) -> Unit) {
    LazyRow(
        modifier = modifier
            .verticalScroll(
                rememberScrollState(),
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.s8)
    ) {
        item {
            Spacer(Modifier.width(Spacing.s8))
        }
        items(filters.size)
        { index ->
            FilterButton(filter = filters[index], onClick = { onFilterClick(index) })
        }
        item {
            Spacer(Modifier.width(Spacing.s8))
        }
    }
}


@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onQueryChanged: (String) -> Unit,
    onSearch: () -> Unit,
    onVoiceInputClicked: () -> Unit
) {
    val textFieldState = rememberTextFieldState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(textFieldState.text.toString()) {
        onQueryChanged(textFieldState.text.toString())
    }
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(RadiusSize.r9999),
        color = MaterialTheme.colorScheme.secondary,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = Spacing.s16),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.s8)
        ) {

            Image(
                painter = painterResource(R.drawable.search_icon),
                contentDescription = null,
            )

            BasicTextField(
                state = textFieldState,
                modifier = Modifier
                    .weight(1f)
                    .border(
                        width = Spacing.border,
                        color = MaterialTheme.colorScheme.outlineVariant,
                    )
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                    )
                    .padding(horizontal = Spacing.s10, vertical = Spacing.s10),
                interactionSource = interactionSource,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),

                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurfaceVariant),

                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                onKeyboardAction = {
                    onQueryChanged(textFieldState.text.toString())
                    onSearch()
                    keyboardController?.hide()
                },
                lineLimits = TextFieldLineLimits.SingleLine,
                decorator = TextFieldDecorator { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (textFieldState.text.isEmpty()) {
                            Text(
                                text = "Search NASA images (e.g. Mars, Nebula)",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                        innerTextField()
                    }
                }
            )
            IconButton(
                modifier = Modifier
                    .padding(vertical = Spacing.s4),
                onClick = onVoiceInputClicked
            )
            {
                Image(
                    painter = painterResource(R.drawable.voice_icon),
                    contentDescription = "voice input button",
                )
            }

        }
    }
}
