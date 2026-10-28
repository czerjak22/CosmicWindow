package com.example.cosmicwindow.feature.today

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cosmicwindow.R
import com.example.cosmicwindow.utils.Spacing
import com.example.cosmicwindow.ui.components.CircleIconButton
import com.example.cosmicwindow.ui.components.ErrorBox


@Composable
fun TodayScreen(
    modifier: Modifier = Modifier, viewModel: TodayViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = modifier.padding(start = Spacing.s16, end = Spacing.s16, top = Spacing.s4)) {

        TodayInSpace(date = "Oct 24, 2024")
        if (uiState.error != null) {

            ErrorBox(
                modifier = Modifier.padding(top = Spacing.s4,bottom = Spacing.s16),
                text = "Unable to reach NASA archive. Check connection.",
                onClick = {})
        }
        else {
            Spacer(Modifier.height(Spacing.s8))
        }
        Column(
            modifier = Modifier.verticalScroll((rememberScrollState())),
            verticalArrangement = Arrangement.spacedBy(Spacing.s16)
        ) {
            Apod()
            PastApodEntryText()
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(Spacing.s16)
            ) {
                items(10) {
                    PreviousApodDay(onClick = { viewModel.updateError() })
                }
            }
            Spacer(modifier = Modifier.height(Spacing.s24))
            FeaturedApodEntryCard()
        }
    }
}

@Preview
@Composable
fun FeaturedApodEntryCard(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(16.dp)
        ),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painterResource(R.drawable.video_icon),
                        contentDescription = null,
                    )

                    Text(
                        "Featured APOD Video", style = MaterialTheme.typography.headlineSmall
                    )
                }
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f),
                    modifier = Modifier.padding(6.dp)

                ) {
                    Text(
                        text = "External Media",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 26.sp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.W500,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp)
                    )
                }
            }
            VideoThumbnailCard()
        }
    }
}


@Composable
fun VideoThumbnailCard(
    videoUrl: String = "", modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(16.dp))
            .clickable { uriHandler.openUri(videoUrl) }) {

        Image(
            painterResource(R.drawable.orion_nebula),
            contentDescription = "Video thumbnail",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Box(
            Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                    )
                )
        )

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(64.dp)
                .background(Color(0xFF38BDF8), CircleShape), contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play",
                tint = Color(0xFF0F172A),
                modifier = Modifier.size(36.dp)
            )
        }


        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "03:42 • 4K Ultra HD",
                color = Color.White,
                style = MaterialTheme.typography.labelMedium
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    Icons.Default.OpenInNew,
                    null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    "YouTube / APOD",
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Composable
fun PastApodEntryText(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            "Previous 7 days",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.W600
        )
        Text(
            "Past APOD Entries",
            color = MaterialTheme.colorScheme.onSecondary,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 26.sp
        )
    }
}

@Composable
fun Apod(
    modifier: Modifier = Modifier,
    onReadClick: () -> Unit = {},
    onBookmarkClick: () -> Unit = {},
    onShareClick: () -> Unit = {}
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
    ) {

        Image(
            painter = painterResource(R.drawable.orion_nebula),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent, Color.Black
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 180.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "OCTOBER 24, 2024",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.labelMedium,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W600,
                )
                Image(
                    painterResource(R.drawable.camera_icon),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                )
                Text(
                    "Hubble Telescope",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 12.sp
                )
            }

            Text(
                "The Great Nebula in Orion",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineLarge,
            )

            Text(
                "Vast cosmic clouds of interstellar gas and stellarghkghgkghkghkkghkghkghkkj dust birth thousand tgdm tgd",
                color = MaterialTheme.colorScheme.onSecondary,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onReadClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        "Read Cosmic Story",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        painterResource(R.drawable.forward_icon),
                        contentDescription = "Go to description",
                        modifier = Modifier.size(12.dp)
                    )
                }

                CircleIconButton(R.drawable.bookmark_icon, "Bookmark", onBookmarkClick)
                CircleIconButton(R.drawable.share_icon, "Share", onShareClick)
            }
        }
    }
}


@Composable
fun PreviousApodDay(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Card(
        onClick = onClick,
        modifier = modifier.width(160.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box {
                Image(
                    painter = painterResource(R.drawable.orion_nebula),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(138f / 96f)
                        .clip(RoundedCornerShape(12.dp))
                )
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f),
                    modifier = Modifier.padding(6.dp)
                ) {
                    Text(
                        text = "Oct 23",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 26.sp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.W600,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    "Yesterday",
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 26.sp),
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Text(
                    "Pillars of Creation",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W500),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun TodayInSpace(modifier: Modifier = Modifier, date: String) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onPrimaryContainer)
        )
        Text(
            text = "Today in Space • $date",
            color = MaterialTheme.colorScheme.onSecondary,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


