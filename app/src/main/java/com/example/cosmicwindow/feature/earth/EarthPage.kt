package com.example.cosmicwindow.feature.earth

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import coil3.imageLoader
import coil3.request.ImageRequest
import com.example.cosmicwindow.R
import com.example.cosmicwindow.core.designsystem.components.AppHeader
import com.example.cosmicwindow.core.designsystem.components.CircleIconButton
import com.example.cosmicwindow.core.designsystem.components.CustomPlayButton
import com.example.cosmicwindow.core.designsystem.components.ErrorBox
import com.example.cosmicwindow.core.designsystem.components.LottieLoader
import com.example.cosmicwindow.core.designsystem.theme.RadiusSize
import com.example.cosmicwindow.core.designsystem.theme.Spacing
import com.example.cosmicwindow.core.permissions.LocationPermissionSettingsDialog
import com.example.cosmicwindow.core.permissions.LocationPermissionState
import com.example.cosmicwindow.core.permissions.rememberLocationPermissionState
import com.example.cosmicwindow.model.Earth
import com.example.cosmicwindow.model.ProjectedPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Composable
fun EarthPage(viewModel: EarthViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val locationPermission = rememberLocationPermissionState()

    LaunchedEffect(locationPermission.state) {
        if (locationPermission.state == LocationPermissionState.GRANTED && uiState.isLocationEnabled) {
            viewModel.toggleLocation(true)
        }
    }

    Scaffold(
        topBar = {
            AppHeader(
                leftIcon = R.drawable.earth_header_icon,
                onClick = {},
                leftIconSize = Spacing.s20,
                rightIconComposable = {
                    Image(
                        painterResource(R.drawable.profile_image),
                        contentDescription = null,
                    )
                },
                mainTitle = stringResource(R.string.earth_page_title),
                secondaryTitle = stringResource(R.string.earth_page_subtitle),
            )
        },
    ) { innerPadding ->

        when {
            uiState.isLoading -> {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    LottieLoader(Modifier.size(Spacing.s48))
                }
                return@Scaffold
            }

            uiState.error != null -> {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    ErrorBox(
                        modifier =
                            Modifier
                                .padding(innerPadding)
                                .padding(horizontal = Spacing.s16),
                        text = uiState.error!!,
                        onClick = { viewModel.loadEarthImages() },
                    )
                }
                return@Scaffold
            }

            uiState.images.isEmpty() -> {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    ErrorBox(text = stringResource(R.string.no_images_found))
                }
                return@Scaffold
            }

            else ->

                Column(
                    modifier =
                        Modifier
                            .padding(innerPadding)
                            .padding(horizontal = Spacing.s16)
                            .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Spacing.s16),
                ) {
                    if (locationPermission.showSettingsDialog) {
                        LocationPermissionSettingsDialog(
                            onOpenSettings = locationPermission.openSettings,
                            onDismiss = locationPermission.dismissSettingsDialog,
                        )
                    }

                    EpicChip(Modifier.align(Alignment.End))
                    PreloadImages(
                        images = uiState.images,
                    )
                    EarthImageDisplay(
                        modifier = Modifier.padding(horizontal = Spacing.s4),
                        imageUrl = uiState.images[uiState.selectedIndex!!].imageUrl,
                        scale = 1.2f,
                        projectedUserPoint = uiState.selectedIndexLocation,
                        isLocationEnabled = uiState.isLocationEnabled,
                        onLocationClick = {
                            when (locationPermission.state) {
                                LocationPermissionState.GRANTED -> {
                                    Log.w("UI", " Location access already granted")
                                    viewModel.toggleLocation()
                                }

                                else -> {
                                    locationPermission.requestPermission()
                                    viewModel.toggleLocation(true)
                                }
                            }
                        },
                    )
                    ControlCard(
                        modifier = Modifier.padding(top = Spacing.s8),
                        captureTime = uiState.images[uiState.selectedIndex!!].date,
                        index = uiState.selectedIndex ?: 0,
                        totalSize = uiState.images.size,
                        indexUpdate = viewModel::updateSelected,
                        startPlayback = viewModel::startPlayback,
                        stopPlayback = viewModel::stopPlayback,
                    )
                    InfoCard(
                        info = uiState.images[uiState.selectedIndex!!].caption,
                    )
                }
        }
    }
}

@Composable
fun InfoCard(modifier: Modifier = Modifier, info: String) {
    Box(
        modifier =
            modifier.background(
                MaterialTheme.colorScheme.surfaceVariant,
                shape =
                    RoundedCornerShape(
                        RadiusSize.r12,
                    ),
            ),
    ) {
        Row(
            modifier = Modifier.padding(Spacing.s16),
            horizontalArrangement = Arrangement.spacedBy(Spacing.s8),
        ) {
            Image(
                painter = painterResource(R.drawable.info_card),
                contentDescription = null,
            )
            Text(
                info,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
}

@Composable
fun ControlCard(
    modifier: Modifier = Modifier,
    captureTime: LocalDateTime,
    index: Int = 8,
    totalSize: Int = 12,
    indexUpdate: (Int) -> Unit,
    startPlayback: () -> Unit,
    stopPlayback: () -> Unit,
) {
    var isPlaying by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(isPlaying, totalSize) {
        if (isPlaying) {
            startPlayback()
        } else {
            stopPlayback()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            stopPlayback()
        }
    }
    Box(
        modifier =
            modifier.background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(RadiusSize.r12),
            ),
    ) {
        Column(
            Modifier.padding(Spacing.s16),
            verticalArrangement = Arrangement.spacedBy(Spacing.s8),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(
                        stringResource(R.string.earth_capture_time_label),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.W500,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        captureTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'")),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.W500,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                Spacer(Modifier.weight(1f))
                Surface(
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(RadiusSize.r9999),
                ) {
                    Row(
                        Modifier.padding(horizontal = Spacing.s12, vertical = Spacing.s6),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.s4),
                    ) {
                        Image(
                            painterResource(R.drawable.accent_dot),
                            contentDescription = null,
                        )
                        Text(
                            stringResource(R.string.earth_slider_chip, index + 1, totalSize),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.W200,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            maxLines = 1,
                        )
                    }
                }
            }

            FrameSlider(
                frameCount = totalSize,
                currentIndex = index,
                onFrameSelected = { index ->
                    stopPlayback()
                    isPlaying = false
                    indexUpdate(index)
                },
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.s24),
            ) {
                Spacer(Modifier.weight(1f))
                CircleIconButton(
                    icon = R.drawable.previous_frame_icon,
                    description = null,
                    onClick = {
                        stopPlayback()
                        isPlaying = false
                        when (index) {
                            0 -> indexUpdate(totalSize - 1)
                            else -> indexUpdate(index - 1)
                        }
                    },
                    iconSize = Spacing.s12,
                )
                CustomPlayButton(
                    onClick = { isPlaying = !isPlaying },
                )
                CircleIconButton(
                    icon = R.drawable.next_frame_icon,
                    description = null,
                    onClick = {
                        stopPlayback()
                        isPlaying = false
                        when (index) {
                            totalSize - 1 -> indexUpdate(0)
                            else -> indexUpdate(index + 1)
                        }
                    },
                    iconSize = Spacing.s12,
                )
                Spacer(Modifier.weight(1f))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrameSlider(
    frameCount: Int,
    currentIndex: Int,
    onFrameSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (frameCount < 2) return

    Column(modifier) {
        Slider(
            value = currentIndex.toFloat(),
            onValueChange = { onFrameSelected(it.roundToInt()) },
            valueRange = 0f..(frameCount - 1).toFloat(),
            steps = frameCount - 2,
            thumb = {
                Box(
                    modifier =
                        Modifier
                            .size(Spacing.s16)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape,
                            ),
                )
            },
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    modifier = Modifier.height(Spacing.s8),
                    colors =
                        SliderDefaults.colors(
                            activeTrackColor = MaterialTheme.colorScheme.tertiary,
                        ),
                    thumbTrackGapSize = 0.dp,
                    drawTick = { _, _ -> },
                    drawStopIndicator = null,
                )
            },
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                stringResource(R.string.earth_slider_frame_1),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondary,
            )
            Text(
                stringResource(R.string.earth_slider_sequence),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.6f),
            )
            Text(
                stringResource(R.string.earth_slider_last_frame, frameCount),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
}

@Composable
fun PreloadImages(images: List<Earth>) {
    val context = LocalContext.current

    LaunchedEffect(images) {
        val loader = context.imageLoader
        images.forEach { url ->
            loader.enqueue(
                ImageRequest.Builder(context).data(url.imageUrl).build(),
            )
        }
    }
}

@Composable
fun EarthImageDisplay(
    modifier: Modifier = Modifier,
    imageUrl: String,
    projectedUserPoint: ProjectedPoint?,
    scale: Float = 1.15f,
    onLocationClick: () -> Unit,
    isLocationEnabled: Boolean,
) {
    val glowColor = MaterialTheme.colorScheme.onPrimaryContainer
    val dotColor = MaterialTheme.colorScheme.primary
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .aspectRatio(1f),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(Spacing.s20)
                    .drawBehind {
                        val r = size.minDimension / 2f
                        val haloRadius = r * 1.2f

                        drawCircle(
                            brush =
                                Brush.radialGradient(
                                    colors =
                                        listOf(
                                            glowColor.copy(alpha = 0.15f),
                                            Color.Transparent,
                                        ),
                                    center = center,
                                    radius = haloRadius,
                                ),
                            radius = haloRadius,
                            center = center,
                        )
                    },
            contentAlignment = Alignment.Center,
        ) {
            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier =
                    Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        },
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    CircleShape,
                                ),
                        contentAlignment = Alignment.Center,
                    ) {
                        LottieLoader(Modifier.padding(Spacing.s16))
                    }
                },
                error = {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    CircleShape,
                                ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            painter = painterResource(R.drawable.no_internet),
                            contentDescription = stringResource(R.string.unavailable_image_description),
                        )
                    }
                },
            )

            if (projectedUserPoint != null && projectedUserPoint.isVisible) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val scaledU = 0.5f + (projectedUserPoint.u - 0.5f) * scale
                    val scaledV = 0.5f + (projectedUserPoint.v - 0.5f) * scale

                    val pinCenter =
                        Offset(
                            x = size.width * scaledU,
                            y = size.height * scaledV,
                        )
                    drawCircle(
                        color = dotColor.copy(alpha = 0.35f),
                        radius = Spacing.s10.toPx(),
                        center = pinCenter,
                    )

                    drawCircle(
                        color = Color.White,
                        radius = Spacing.s6.toPx(),
                        center = pinCenter,
                    )
                    drawCircle(
                        color = dotColor,
                        radius = Spacing.s4.toPx(),
                        center = pinCenter,
                    )
                }
            }
        }

        Box(Modifier.matchParentSize(), contentAlignment = Alignment.BottomEnd) {
            CircleIconButton(
                icon =
                    when (isLocationEnabled) {
                        true -> R.drawable.location_on_icon
                        false -> R.drawable.location_off_icon
                    },
                description = stringResource(R.string.earh_turn_on_location_button_description),
                onClick = onLocationClick,
            )
        }
    }
}

@Composable
fun EpicChip(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier.background(
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                shape = RoundedCornerShape(RadiusSize.r9999),
            ),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = Spacing.s12, vertical = Spacing.s4),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.s6),
        ) {
            Image(
                painter = painterResource(R.drawable.cloud_saved),
                contentDescription = null,
            )
            Text(
                stringResource(R.string.earth_epic_feed_text),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
}
