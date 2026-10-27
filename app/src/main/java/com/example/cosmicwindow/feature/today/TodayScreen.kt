package com.example.cosmicwindow.feature.today

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cosmicwindow.R
import com.example.cosmicwindow.ui.components.CircleIconButton


@Composable
fun TodayScreen(
    modifier: Modifier = Modifier,
    viewModel: TodayViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        TodayHeader()
        TodayInSpace(date = "Oct 24, 2024")
        if (uiState.error != null) {
            Spacer(Modifier.height(8.dp))
            ErrorBox()
        }
        Column(
            modifier = Modifier.verticalScroll((rememberScrollState())),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Apod()
            PastApodEntryText()
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(10)
                {
                    PreviousApodDay(onClick = { viewModel.updateError() })
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
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
                            Color.Transparent,
                            Color.Black
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
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
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
                    modifier = Modifier
                        .padding(6.dp)
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

@Composable
fun TodayHeader(modifier: Modifier = Modifier) {

    Row(
        modifier = modifier
            .fillMaxWidth(),

        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.header_image),
                contentDescription = "Today Header's icon",

                modifier = Modifier
                    .padding(start = 16.dp, end = 8.dp)
                    .size(60.dp),
                contentScale = ContentScale.Fit
            )
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    "Cosmic Window",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    "ASTRONOMY PICTURE OF THE DAY",
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
        IconButton({}, modifier = Modifier.padding(end = 16.dp)) {
            Icon(
                painterResource(R.drawable.calendar_icon),
                contentDescription = "Day set icon",
                tint = MaterialTheme.colorScheme.onSecondary,
            )
        }

    }
}

@Composable
fun ErrorBox(modifier: Modifier = Modifier) {

    Box(
        modifier = modifier
            .border(
                border = BorderStroke(
                    1.dp,
                    color = MaterialTheme.colorScheme.error.copy(alpha = 0.3f),
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painterResource(R.drawable.no_internet),
                contentDescription = "No internet icon",
                modifier = Modifier.padding(top = 27.dp, bottom = 27.dp)
            )
            Text(
                "Unable to reach NASA archivhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhe.",
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 12.sp,
                fontWeight = FontWeight.W400,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )


            Button(
                onClick = {},
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.25f),
                    contentColor = MaterialTheme.colorScheme.onError
                ),

                ) {
                Icon(
                    painter = painterResource(R.drawable.reload_icon),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    "Retry",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W500
                )

            }
        }
    }
}
