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
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.cosmicwindow.R
import com.example.cosmicwindow.model.Apod
import com.example.cosmicwindow.ui.components.AppHeader
import com.example.cosmicwindow.ui.theme.Spacing
import com.example.cosmicwindow.ui.components.CircleIconButton
import com.example.cosmicwindow.ui.components.CurrentDatePicker
import com.example.cosmicwindow.ui.components.ErrorBox
import com.example.cosmicwindow.ui.theme.RadiusSize
import com.example.cosmicwindow.utils.getDayTextSince
import java.time.format.DateTimeFormatter

@Composable
fun TodayScreen(
    modifier: Modifier = Modifier, viewModel: TodayViewModel = hiltViewModel()
) {

    var showDatePicker by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(), topBar = {
            AppHeader(
                modifier = Modifier,
                leftIcon = R.drawable.header_image,
                rightButtonIcon = R.drawable.calendar_icon,
                onClick = { showDatePicker = true },
            )
        }) { innerPadding ->

        if (showDatePicker) {
            CurrentDatePicker(
                onDateSelected = { selectedDate -> viewModel.updateCurretDay(selectedDate) },
                onDismiss = { showDatePicker = false })
        }

        Column(
            modifier = Modifier.padding(innerPadding)
            // .padding(start = Spacing.s16, end = Spacing.s16, top = Spacing.s4)
        ) {

            TodayInSpace(
                Modifier.padding(horizontal = Spacing.s16), date = uiState.selectedDate.format(
                    DateTimeFormatter.ofPattern("MMM dd, yyyy")
                )
            )
            if (uiState.error != null) {

                ErrorBox(
                    modifier = Modifier.padding(
                        top = Spacing.s4,
                        bottom = Spacing.s16,
                        start = Spacing.s16,
                        end = Spacing.s16,
                    ), text = uiState.error!!, onClick = {})
            } else {
                Spacer(Modifier.height(Spacing.s8))
            }
            if (uiState.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            if (!uiState.isLoading && uiState.apods.isEmpty()) {
                ErrorBox(
                    modifier = Modifier.padding(
                        top = Spacing.s4,
                        bottom = Spacing.s16,
                        start = Spacing.s16,
                        end = Spacing.s16,
                    ),
                    text = "Something went wrong with getteing data",
                    //TODO az isloadingot majd resetaljam itt
                    //TODO Error kezeles internet nelkul crashel
                    onClick = viewModel::getCurrentApods
                )

                return@Scaffold
            }

            Column(
                modifier = Modifier.verticalScroll((rememberScrollState())),
                verticalArrangement = Arrangement.spacedBy(Spacing.s24)
            ) {
                if (uiState.apods.isEmpty()) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Text(
                            "No apods to show!",
                            style = MaterialTheme.typography.headlineLarge
                        )
                    }
                    return@Scaffold
                }

                CurrentApod(Modifier.padding(horizontal = Spacing.s16), uiState.apods.first())
                PreviousApodStripe(Modifier, apods = uiState.apods, onClick = {})
                if (uiState.featuredVideoApod != null) {
                    FeaturedApodVideoCard(
                        Modifier.padding(horizontal = Spacing.s16),
                        apod = uiState.featuredVideoApod!!,
                        //onClick = {},
                    )
                }
                //TODO megkerdezni hogy mit csinaljak
                DataCard(Modifier.padding(horizontal = Spacing.s16))
            }
        }

    }
}

@Composable
fun DataCard(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(16.dp),

            ),
        border = BorderStroke(
            Spacing.border, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(RadiusSize.r16),
    ) {
        Column(
            Modifier.padding(Spacing.s16),

            verticalArrangement = Arrangement.spacedBy(Spacing.s8)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(R.drawable.original_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(Spacing.s24)
                        .padding(end = Spacing.s8)
                )
                Text(
                    "Public NASA Data • APOD Archive",
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 26.sp),
                    fontWeight = FontWeight.W500,
                    color = MaterialTheme.colorScheme.onSecondary,
                )
            }

            Text(
                text = "Image Credit: ESA / Hubble, NASA • Processed by Robert Gendler",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Box(
                Modifier.background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(RadiusSize.r9999)
                )
            ) {
                Text(
                    text = "Independent educational explorer • Not affiliated with NASA",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(
                        start = Spacing.s24,
                        end = Spacing.s24,
                        top = Spacing.s6,
                        bottom = Spacing.s6
                    )
                )
            }

        }
    }
}

@Composable
private fun PreviousApodStripe(
    modifier: Modifier = Modifier, apods: List<Apod>, onClick: () -> Unit
) {
    Column(modifier) {
        PastApodEntryText(
            Modifier.padding(
                bottom = Spacing.s8,
                start = Spacing.s16,
                end = Spacing.s16,
            )
        )
        LazyRow(
            modifier = Modifier.padding(start = Spacing.s16, bottom = Spacing.s8),
            horizontalArrangement = Arrangement.spacedBy(Spacing.s8)
        ) {
            items(apods.size - 1) {
                PreviousApodDay(apod = apods[it + 1], onClick = onClick)
            }
        }
    }
}


@Composable
fun FeaturedApodVideoCard(
    modifier: Modifier = Modifier,
    apod: Apod,
) {
    val videoHandler = LocalUriHandler.current;
    Surface(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(16.dp)
        ),
        shape = RoundedCornerShape(RadiusSize.r16),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.s16)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f),
                ) {

                    Image(
                        painter = painterResource(R.drawable.video_icon), contentDescription = null
                    )

                    Text(
                        "Featured APOD Video",
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 1,
                    )
                }
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f),
                    modifier = Modifier.padding(Spacing.s10)
                ) {
                    Text(
                        text = "External Media",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 26.sp),
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        fontWeight = FontWeight.W500,
                        maxLines = 1,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp)
                    )
                }
            }

            VideoThumbnailCard(
                modifier = Modifier.padding(bottom = Spacing.s8),
                apod = apod,
                onClick = { videoHandler.openUri(apod.videoUrl ?: apod.hdurl ?: apod.url) })
            Text(
                apod.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = Spacing.s8)
            )
            Text(
                apod.alt,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = Spacing.s8)
            )

            Button(
                onClick = {
                    videoHandler.openUri(apod.permalink)
                }, colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ), border = BorderStroke(
                    Spacing.border,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                ), shape = RoundedCornerShape(RadiusSize.r12)

            ) {

                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Open video in browser",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(
                            end = Spacing.s8, top = Spacing.s10, bottom = Spacing.s10
                        )
                    )
                    Icon(
                        painter = painterResource(R.drawable.open_elsewhere_icon),
                        contentDescription = "Open in external player",
                        Modifier.size(Spacing.s12)
                    )

                }
            }
        }
    }
}


@Composable
fun VideoThumbnailCard(
    modifier: Modifier = Modifier,
    apod: Apod,
    onClick: () -> Unit,
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(RadiusSize.r12))
            .clickable { onClick() }) {
        AsyncImage(
            apod.hdurl ?: apod.url,
            contentDescription = apod.alt,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Box(
            Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f),
                        )
                    )
                )
        )

        Box(
            modifier = Modifier.run {
                align(Alignment.Center)
                    .size(Spacing.s56)
                    .background(
                        color = MaterialTheme.colorScheme.primary, shape = CircleShape
                    )
            }, contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.play_icon),
                contentDescription = "Play",
            )
        }


        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(
                    start = Spacing.s12, end = Spacing.s12, bottom = Spacing.s10
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,

            ) {
            //TODO mit csinaljak kerdezzem meg
            Text(
                "honnan?",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.open_elsewhere_icon),
                    null,
                )
                Text(
                    "YouTube / APOD",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall
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
        horizontalArrangement = Arrangement.spacedBy(Spacing.s8),
    ) {
        Image(

            painter = painterResource(
                R.drawable.table_icon
            ),
            contentDescription = null,
        )
        Text(
            "Previous 7 days",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.W600

        )
        Spacer(Modifier.weight(1f))
        Text(
            "Past APOD Entries",
            color = MaterialTheme.colorScheme.onSecondary,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 26.sp
        )
    }
}

@Composable
fun CurrentApod(
    modifier: Modifier = Modifier,
    apod: Apod,
    onReadClick: () -> Unit = {},
    onBookmarkClick: () -> Unit = {},
    onShareClick: () -> Unit = {}
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
    ) {

        AsyncImage(
            model = apod.hdurl ?: apod.url,
            contentDescription = apod.alt,
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
                .padding(
                    top = 180.dp, start = Spacing.s16, end = Spacing.s16, bottom = Spacing.s16
                ), verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.s8)
            ) {
                Text(
                    apod.date.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W600,
                )
                Image(
                    painterResource(R.drawable.camera_icon),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                )
                Text(
                    "•",
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    apod.credit,
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Text(
                apod.title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineLarge,
            )

            Text(
                apod.alt,
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
    apod: Apod,
    onClick: () -> Unit = {},
) {
    Card(
        onClick = onClick,
        modifier = modifier.width(160.dp),
        shape = RoundedCornerShape(RadiusSize.r16),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        border = BorderStroke(Spacing.border, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(Spacing.s12),
            verticalArrangement = Arrangement.spacedBy(Spacing.s12)
        ) {
            Box {

                AsyncImage(
                    model = apod.hdurl ?: apod.url,
                    contentDescription = apod.alt,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(138f / 96f)
                        .clip(RoundedCornerShape(RadiusSize.r12))
                )
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f),
                    modifier = Modifier.padding(Spacing.s6)
                ) {
                    Text(
                        text = apod.date.format(DateTimeFormatter.ofPattern("MMM dd")),
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 26.sp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.W600,
                        modifier = Modifier.padding(horizontal = Spacing.s8, vertical = Spacing.s8)
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    getDayTextSince(apod.date),
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 26.sp),
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Text(
                    apod.title,
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


