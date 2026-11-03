package com.example.cosmicwindow.feature.earth

import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.cosmicwindow.R
import com.example.cosmicwindow.ui.components.AppHeader
import com.example.cosmicwindow.ui.components.CircleIconButton
import com.example.cosmicwindow.ui.components.CustomPlayButton
import com.example.cosmicwindow.ui.theme.RadiusSize
import com.example.cosmicwindow.ui.theme.Spacing
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Composable
fun EarthPage(modifier: Modifier = Modifier) {
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
                secondaryTitle = "DSCOVR EPIC"
            )
        }

    ) { innerPadding ->
        Column(

            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = Spacing.s16),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.s16)
        ) {
            EpicChip(Modifier.align(Alignment.End))
            EarthImageDisplay()
            ControlCard(
                modifier = Modifier.padding(top = Spacing.s8),
                captureTime = LocalDateTime.now()
            )
        }
    }
}


@Composable
fun ControlCard(
    modifier: Modifier = Modifier,
    captureTime: LocalDateTime,
    index: Int = 8,
    totalSize: Int = 12
) {
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(RadiusSize.r12)
            )
            .border(
                Spacing.border, MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(RadiusSize.r12)
            )

    )
    {
        Column(
            Modifier.padding(Spacing.s16),
            verticalArrangement = Arrangement.spacedBy(Spacing.s8)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column() {
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
                Surface(color = MaterialTheme.colorScheme.tertiary) {
                    Row(
                        Modifier.padding(horizontal = Spacing.s12, vertical = Spacing.s6),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painterResource(R.drawable.accent_dot),
                            contentDescription = null,
                        )
                        Text(
                            "Frame $index of $totalSize",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }
                }
            }

            FrameSlider(
                frameCount = 13,
                currentIndex = 2,
                onFrameSelected = {  }
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.s8)
            ) {
                Spacer(Modifier.weight(1f))
                CircleIconButton(
                    icon = R.drawable.previous_frame_icon,
                    description = null,
                    onClick = {},
                    iconSize = Spacing.s12
                )
                CustomPlayButton(onClick = {})
                CircleIconButton(
                    icon = R.drawable.next_frame_icon,
                    description = null,
                    onClick = {},
                    iconSize = Spacing.s12
                )
                Spacer(Modifier.weight(1f))
            }

        }
    }
}

@Composable
fun FrameSlider(
    frameCount: Int,
    currentIndex: Int,
    onFrameSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (frameCount < 2) return

    Column(modifier) {
        Slider(
            value = currentIndex.toFloat(),
            onValueChange = { onFrameSelected(it.roundToInt()) },
            valueRange = 0f..(frameCount - 1).toFloat(),
            steps = frameCount - 2
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Frame 1")
            Text("Sequence: 24h")
            Text("Frame $frameCount")
        }
    }
}

@Composable
fun EarthImageDisplay(
    modifier: Modifier = Modifier,
    imageUrl: String = "https://epic.gsfc.nasa.gov/archive/natural/2026/10/29/png/epic_1b_20261029000830.png",
    scale: Float = 1.15f
) {
    val glowColor = MaterialTheme.colorScheme.onPrimaryContainer
    Box(modifier = modifier.fillMaxWidth()) {
        Box(
            Modifier
                .padding(horizontal = Spacing.s20)
                .drawBehind {
                    val r = size.minDimension / 2f
                    val haloRadius = r * 1.15f
                    val edge = r / haloRadius

                    drawCircle(
                        brush = Brush.radialGradient(
                            (edge - 0.05f) to glowColor.copy(
                                alpha = 0.2f
                            ),
                            1f to Color.Transparent,
                            center = center,
                            radius = haloRadius
                        ),
                        radius = haloRadius,
                        center = center
                    )

                }
                .clip(CircleShape)

        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Earth Image",
                Modifier.graphicsLayer(scaleX = scale, scaleY = scale)
            )
        }
    }
}

@Composable
fun EpicChip(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                shape = RoundedCornerShape(RadiusSize.r9999)
            )

    ) {
        Row(
            modifier = Modifier.padding(horizontal = Spacing.s12, vertical = Spacing.s4),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.s6)
        ) {
            Image(
                painter = painterResource(R.drawable.cloud_saved),
                contentDescription = null,
            )
            Text(
                "EPIC Feed",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondary
            )

        }
    }
}