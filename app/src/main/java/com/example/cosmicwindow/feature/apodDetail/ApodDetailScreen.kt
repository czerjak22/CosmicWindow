package com.example.cosmicwindow.feature.apodDetail


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.cosmicwindow.R
import com.example.cosmicwindow.model.Apod
import com.example.cosmicwindow.ui.components.AppHeader
import com.example.cosmicwindow.ui.theme.Spacing
import com.example.cosmicwindow.utils.shareLink

@Composable
fun ApodDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: ApodDetailViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navcontroller = rememberNavController()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            StoryHeader(
                onShare = {
                    when {
                        uiState.apod != null -> {
                            context.shareLink(uiState.apod!!.permalink)
                        }

                        else -> {}
                    }
                },
                onClickBack = { navcontroller.popBackStack() },
                onBookmark = {},
            )

        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            Text("APOD IMPL")
        }
    }
}

@Preview
@Composable
fun StoryHeader(
    modifier: Modifier = Modifier,
    onClickBack: () -> Unit = {},
    onBookmark: () -> Unit = {},
    onShare: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.s4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onClickBack
        ) {
            Image(
                painter = painterResource(R.drawable.back_icon),
                contentDescription = "backButton",
            )
        }
        Text(
            "APOD Story Detail",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.weight(1f))
        IconButton(
            onClick = onBookmark
        ) {
            Image(
                painter = painterResource(R.drawable.bookmark_icon),
                contentDescription = "bookmarkButton",

                )
        }
        IconButton(
            onClick = onShare
        ) {
            Image(
                painter = painterResource(R.drawable.share_icon),
                contentDescription = "shareButton",

                )
        }
    }
}