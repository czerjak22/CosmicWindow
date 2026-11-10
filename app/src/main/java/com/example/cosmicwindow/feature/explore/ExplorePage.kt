package com.example.cosmicwindow.feature.explore

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.model.content.GradientColor
import com.example.cosmicwindow.R
import com.example.cosmicwindow.core.designsystem.components.AppHeader
import com.example.cosmicwindow.core.designsystem.components.ErrorBox
import com.example.cosmicwindow.core.designsystem.components.LottieLoader
import com.example.cosmicwindow.core.designsystem.theme.RadiusSize
import com.example.cosmicwindow.core.designsystem.theme.Spacing
import com.example.cosmicwindow.model.FilterState

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

        // todo refresh hook impl
        if (uiState.error != null) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                ErrorBox(text = uiState.error!!, onClick = { })
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(vertical = Spacing.s16),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.s8),
        ) {
            SearchBar(
                modifier = Modifier.padding(horizontal = Spacing.s16),
                onSearch = viewModel::toggleLoading,
                onVoiceInputClicked = {})

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(), contentAlignment = Alignment.Center
                ) {
                    LottieLoader(Modifier.padding(Spacing.s20))
                }
                return@Scaffold
            }

            FilterBar(
                onFilterClick = viewModel::toggleFilter,
                filters = uiState.filters,
            )
            ImageGrid(Modifier.fillMaxSize())
        }
    }
}


@Composable
fun CardElement(
    modifier: Modifier = Modifier,
    id: String = "PIA25301",
    title: String = "Perseverance valmi valami",
    date: String = "Jul 12, 2022",
    badgeText: String = "M16",
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(RadiusSize.r16)
    Surface(
        modifier = modifier
            .clip(shape)
            .clickable(onClick = onClick),
        shape = shape,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column {
            Box {
                Image(
                    painter = painterResource(R.drawable.orion_nebula),
                    contentDescription = title,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
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
                        text = id,
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
                    text = title,
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
                        text = date,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.earth_icon),
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.width(Spacing.s4))
                        Text(
                            text = badgeText,
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
fun ImageGrid(modifier: Modifier = Modifier) {
    LazyVerticalStaggeredGrid(
        modifier = modifier,
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = Spacing.s8,
        horizontalArrangement = Arrangement.spacedBy(Spacing.s8),
    ) {
        items(10)
        {
            CardElement(onClick = {})
        }

    }
}

@Composable
fun FilterButton(filter: FilterState, onClick: () -> Unit) {

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
fun FilterBar(modifier: Modifier = Modifier, filters: List<FilterState>, onFilterClick: (Int) -> Unit) {
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
fun SearchBar(modifier: Modifier = Modifier, onSearch: () -> Unit, onVoiceInputClicked: () -> Unit) {
    val textFieldState = rememberTextFieldState()
    val keyboardController = LocalSoftwareKeyboardController.current

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
                contentDescription = null
            )

            Box(
                modifier = Modifier
                    .padding(vertical = Spacing.s8)
                    .border(Spacing.border, color = MaterialTheme.colorScheme.outline)
                    .weight(1f),
            ) {
                TextField(
                    state = textFieldState,
                    colors = TextFieldDefaults.colors(
                        unfocusedTextColor = MaterialTheme.colorScheme.outlineVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.secondary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.secondary,

                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium,

                    placeholder = {
                        Text(
                            "Search NASA images (e.g. Mars, Nebula)...",
                            maxLines = 1,
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    onKeyboardAction = {
                        onSearch()
                        keyboardController?.hide()
                    },
                    lineLimits = TextFieldLineLimits.SingleLine,
                )
            }


            IconButton(
                modifier = Modifier
                    .padding(vertical = Spacing.s16),
                onClick = onVoiceInputClicked
            )
            {
                Image(
                    painter = painterResource(R.drawable.voice_icon),
                    contentDescription = "voice input button"
                )
            }

        }
    }
}
