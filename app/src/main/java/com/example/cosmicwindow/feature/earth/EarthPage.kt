package com.example.cosmicwindow.feature.earth

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import com.example.cosmicwindow.R
import com.example.cosmicwindow.core.designsystem.components.AppHeader
import com.example.cosmicwindow.core.designsystem.components.CircleIconButton
import com.example.cosmicwindow.core.designsystem.components.CustomPlayButton
import com.example.cosmicwindow.core.designsystem.components.ErrorBox
import com.example.cosmicwindow.core.designsystem.components.LottieLoader
import com.example.cosmicwindow.core.designsystem.theme.RadiusSize
import com.example.cosmicwindow.core.designsystem.theme.Spacing
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun EarthPage(viewModel: EarthViewModel = hiltViewModel()) {
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
                mainTitle = "Earth in Motion",
                secondaryTitle = "DSCOVR EPIC",
            )
        },
    ) { innerPadding ->

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                LottieLoader(Modifier.padding(Spacing.s16))
            }
            return@Scaffold
        }

        if (uiState.error != null) {
            Box(modifier = Modifier.fillMaxWidth().padding(innerPadding), contentAlignment = Alignment.Center) {
                ErrorBox(text = uiState.error!!, onClick = { viewModel.loadEarthImages() })
            }
            return@Scaffold
        }
        if (uiState.images.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().padding(innerPadding), contentAlignment = Alignment.Center) {
                ErrorBox(text = "No Images found!")
            }
            return@Scaffold
        }

        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .padding(horizontal = Spacing.s16, vertical = Spacing.s16)
                    .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.s16),
        ) {
            EpicChip(Modifier.align(Alignment.End))
            EarthImageDisplay(
                modifier = Modifier.padding(horizontal = Spacing.s4),
                imageUrl = uiState.images[uiState.selectedIndex!!].imageUrl,
                scale = 1.2f,
            )
            ControlCard(
                modifier = Modifier.padding(top = Spacing.s8),
                captureTime = uiState.images[uiState.selectedIndex!!].date,
                index = uiState.selectedIndex ?: 0,
                totalSize = uiState.images.size,
                indexUpdate = viewModel::updateSelected,
            )
            InfoCard(
                info =
                    uiState.images[uiState.selectedIndex!!].caption,
            )
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
            modifier =
                Modifier
                    .padding(Spacing.s16),
            horizontalArrangement = Arrangement.spacedBy(Spacing.s8),
        ) {
            Image(
                painter = painterResource(R.drawable.info_card),
                contentDescription = "Info card",
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
) {
    var isPlaying by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(isPlaying, index, totalSize) {
        if (isPlaying && totalSize > 1) {
            delay(500.milliseconds)
            indexUpdate((index + 1) % totalSize)
        }
    }

    Box(
        modifier =
            modifier
                .background(
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
                        "CAPTURE TIME",
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
                            "Frame ${index + 1} of $totalSize",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.W200,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }
                }
            }

            FrameSlider(
                frameCount = totalSize,
                currentIndex = index,
                onFrameSelected = indexUpdate,
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
                "Frame 1",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondary,
            )
            Text(
                "Sequence: 24h",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.6f),
            )
            Text(
                "Frame $frameCount",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
}

@Composable
fun EarthImageDisplay(
    modifier: Modifier = Modifier,
    imageUrl: String = "https://epic.gsfc.nasa.gov/archive/natural/2026/10/29/png/epic_1b_20261029000830.png",
    scale: Float = 1.15f,
) {
    val glowColor = MaterialTheme.colorScheme.onPrimaryContainer

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
                    }.clip(CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = "Earth Image",
                modifier =
                    Modifier
                        .fillMaxSize()
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
                            contentDescription = "Image unavailable",
                        )
                    }
                },
            )
        }
    }
}

@Composable
fun EpicChip(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .background(
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
                "EPIC Feed",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
}
