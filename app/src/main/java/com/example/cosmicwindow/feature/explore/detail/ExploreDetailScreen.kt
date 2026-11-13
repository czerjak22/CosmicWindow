package com.example.cosmicwindow.feature.explore.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextOverflow
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

@Composable
fun ExploreDetailScreen(
    viewModel: ExploreDetailViewModel = hiltViewModel(), onClickBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold { innerPadding ->

        if (uiState.error != null) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                ErrorBox(text = uiState.error!!, onClick = viewModel::onErrorRetry)
            }
            return@Scaffold
        }

        if (uiState.isLoading) {
            Box(
                Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                LottieLoader()
            }
            return@Scaffold
        }
        if (uiState.error != null) {
            Box(
                Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                ErrorBox(text = uiState.error!!)
            }
            return@Scaffold
        }

        ImagePage(
            modifier = Modifier.padding(innerPadding),
            onBackPressed = onClickBack,
            searchResult = uiState.searchItem!!,
            onPictureLoaded = { viewModel.pictureLoaded() })

        if (!uiState.pictureLoaded) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center,
            ) { LottieLoader() }
        }
    }
}

@Composable
fun Details(modifier: Modifier = Modifier, searchResult: SearchResultItem) {

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.s8),
    ) {

        Text(
            searchResult.title,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            AnnotatedString.fromHtml(searchResult.description),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 8,
            overflow = TextOverflow.Ellipsis
        )

        Box(
            Modifier.background(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
                shape = RoundedCornerShape(RadiusSize.r12)
            )
        ) {
            Row(
                Modifier
                    .padding(Spacing.s16)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    Spacing.s8, alignment = Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically,

                ) {

                ColumnElement(
                    title = "Observation Date",
                    content = searchResult.dateCreated.format(DateTimeFormatter.ofPattern("MMM dd, yyy"))
                )
                if (searchResult.center != null) {
                    ColumnElement(
                        title = "Center",
                        content = searchResult.center,
                    )
                }
                //            ColumnElement(
                //                title = "Photographer",
                //                content = searchResult.
                //            )
            }
        }
    }
}

@Composable
fun ColumnElement(modifier: Modifier = Modifier, title: String, content: String) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.s8),
    ) {

        Text(
            title,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.W500,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            AnnotatedString.fromHtml(content),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun ImagePage(
    searchResult: SearchResultItem,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    onPictureLoaded: () -> Unit,
) {

    Box(modifier = modifier.fillMaxSize()) {


        AsyncImage(
            model = searchResult.originalUrl ?: searchResult.thumbnailUrl,
            contentDescription = searchResult.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            onSuccess = {
                onPictureLoaded()
            }

        )

        Box(
            Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        0.6f to Color.Transparent,
                        0.8f to MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.85f),
                        1f to MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )
        )


        CircleIconButton(
            icon = R.drawable.exit_icon,
            description = "Back button",
            onClick = onBackPressed,
            backgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(Spacing.s16),
            iconSize = Spacing.s14
        )

        Details(
            searchResult = searchResult,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(Spacing.s16),
        )
    }
}
