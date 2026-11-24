package com.example.cosmicwindow.feature.apoddetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import com.example.cosmicwindow.R
import com.example.cosmicwindow.core.designsystem.components.CircleIconButton
import com.example.cosmicwindow.core.designsystem.components.ErrorBox
import com.example.cosmicwindow.core.designsystem.components.ExternalLinkOpenerButton
import com.example.cosmicwindow.core.designsystem.components.LottieLoader
import com.example.cosmicwindow.core.designsystem.theme.RadiusSize
import com.example.cosmicwindow.core.designsystem.theme.Spacing
import com.example.cosmicwindow.core.utils.shareLink
import com.example.cosmicwindow.model.Apod

@Composable
fun ApodDetailsScreen(viewModel: ApodDetailViewModel = hiltViewModel(), onClickBack: () -> Unit) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showFullscreen by remember { mutableStateOf(false) }

    val shareApod = { if (uiState.apod != null) context.shareLink(uiState.apod!!.permalink) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            StoryHeader(
                onShare = { shareApod() },
                onClickBack = onClickBack,
                onBookmark = {
                    if (uiState.apod != null) {
                        viewModel.toggleApodSave()
                    }
                },
                isSaved = uiState.apod?.isSaved ?: false,
            )
        },
    ) { innerPadding ->

        when {
            uiState.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    LottieLoader(Modifier.size(Spacing.s56))
                }
                return@Scaffold
            }

            uiState.error != null -> {
                ErrorBox(
                    modifier =
                        Modifier
                            .padding(innerPadding)
                            .padding(horizontal = Spacing.s16),
                    text = uiState.error!!,
                    onClick = { viewModel.loadApod() },
                )
                return@Scaffold
            }

            uiState.apod == null -> {
                return@Scaffold
            }

            else -> {
                Column(
                    modifier =
                        Modifier
                            .padding(innerPadding)
                            .verticalScroll((rememberScrollState())),
                ) {
                    if (showFullscreen) {
                        FullscreenImageDialog(
                            imageUrl = uiState.apod!!.hdUrl ?: uiState.apod!!.url,
                            onDismiss = { showFullscreen = false },
                        )
                    }
                    ApodImage(
                        imageUri = uiState.apod!!.hdUrl ?: uiState.apod!!.url,
                        onFullscreenClick = { showFullscreen = true },
                    )

                    Column(
                        Modifier.padding(horizontal = Spacing.s16),
                        verticalArrangement = Arrangement.spacedBy(Spacing.s16),
                    ) {
                        Text(
                            uiState.apod!!.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                        if (uiState.apod!!.videoUrl != null) {
                            VideoCard(url = uiState.apod!!.videoUrl!!)
                        }

                        Text(
                            stringResource(R.string.explanation_label),
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                        Text(
                            AnnotatedString.fromHtml(uiState.apod!!.explanation),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                        DetailsCard(apod = uiState.apod!!)
                        ShareButton(onClick = shareApod)
                    }
                }
            }
        }
    }
}

@Composable
fun ShareButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
                shape = RoundedCornerShape(RadiusSize.r9999),
            ),
    ) {
        Button(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.s10, vertical = Spacing.s8),
            onClick = onClick,
        ) {
            Text(
                stringResource(R.string.share_story_button),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.W600,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@Composable
fun VideoCard(modifier: Modifier = Modifier, url: String) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.secondary,
        shape = RoundedCornerShape(RadiusSize.r12),
        border =
            BorderStroke(
                Spacing.border,
                color = MaterialTheme.colorScheme.outline,
            ),
    ) {
        Row(
            modifier = Modifier.padding(Spacing.s16),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.s8),
        ) {
            CircleIconButton(
                icon = R.drawable.external_player_icon,
                description = null,
                onClick = {},
                size = 40.dp,
            )

            Text(
                stringResource(R.string.details_video_card_title),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier =
                    Modifier
                        .weight(1f)
                        .basicMarquee(),
            )

            ExternalLinkOpenerButton(
                videoHandler = LocalUriHandler.current,
                url = url,
                buttonText = stringResource(R.string.open_video_button),
            )
        }
    }
}

@Composable
fun DetailsCard(modifier: Modifier = Modifier, apod: Apod) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(RadiusSize.r16),
        color = MaterialTheme.colorScheme.secondary,
        border =
            BorderStroke(
                Spacing.border,
                MaterialTheme.colorScheme.outline,
            ),
    ) {
        Column(modifier = Modifier.padding(Spacing.s16)) {
            Text(
                stringResource(R.string.attribution_label),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.W600,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )

            InfoRow(stringResource(R.string.source_label), stringResource(R.string.nasa_apod_api))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            InfoRow(
                stringResource(R.string.credit_label),
                AnnotatedString.fromHtml(apod.credit).text,
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            InfoRow(
                stringResource(R.string.copyright_label),
                AnnotatedString.fromHtml(apod.copyright ?: apod.credit).text,
                valueColor = MaterialTheme.colorScheme.onTertiaryContainer,
            )
            ExternalLinkOpenerButton(
                modifier = Modifier.fillMaxWidth(),
                videoHandler = LocalUriHandler.current,
                url = apod.permalink,
                buttonText = stringResource(R.string.view_original_apod_button_label),
            )
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = valueColor,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
fun FullscreenImageDialog(imageUrl: String, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties =
            DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false,
            ),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color.Black),
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = stringResource(R.string.fullscreen_image_description),
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize(),
            )

            IconButton(
                onClick = onDismiss,
                modifier =
                    Modifier
                        .align(Alignment.TopEnd)
                        .statusBarsPadding()
                        .padding(8.dp),
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = stringResource(R.string.close_label),
                    tint = Color.White,
                )
            }
        }
    }
}

@Composable
fun ApodImage(
    modifier: Modifier = Modifier,
    imageUri: String,
    onFullscreenClick: () -> Unit = {},
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier.fillMaxWidth(),
            model = imageUri,
            contentScale = ContentScale.FillWidth,
            contentDescription = stringResource(R.string.apod_image_description),
            loading = {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f),
                    contentAlignment = Alignment.Center,
                ) {
                    LottieLoader(Modifier.size(Spacing.s56))
                }
            },
        )

        Box(
            Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors =
                            listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                                MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
                                MaterialTheme.colorScheme.background,
                            ),
                    ),
                ),
        )

        Box(
            modifier = Modifier.matchParentSize(),
            contentAlignment = Alignment.TopEnd,
        ) {
            CircleIconButton(
                modifier = Modifier.padding(top = Spacing.s8, end = Spacing.s16),
                icon = R.drawable.fullscreen_icon,
                description = stringResource(R.string.fullscreen_image_description),
                onClick = onFullscreenClick,
                backgroundColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f),
            )
        }
    }
}

@Composable
fun StoryHeader(
    modifier: Modifier = Modifier,
    onClickBack: () -> Unit = {},
    onBookmark: () -> Unit = {},
    onShare: () -> Unit = {},
    isSaved: Boolean,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .statusBarsPadding(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.s4),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onClickBack,
        ) {
            Image(
                painter = painterResource(R.drawable.back_icon),
                contentDescription = stringResource(R.string.back_button_description),
            )
        }
        Text(
            stringResource(R.string.apod_story_detail),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.weight(1f))
        IconButton(
            onClick = onBookmark,
        ) {
            Image(
                painter =
                    painterResource(
                        if (isSaved) R.drawable.bookmark_filled_icon else R.drawable.bookmark_icon,
                    ),
                contentDescription = stringResource(R.string.bookmark_description),
            )
        }
        IconButton(
            onClick = onShare,
        ) {
            Image(
                painter = painterResource(R.drawable.share_icon),
                contentDescription = stringResource(R.string.share_story_button),
            )
        }
    }
}
