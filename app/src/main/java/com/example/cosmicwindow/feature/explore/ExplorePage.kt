package com.example.cosmicwindow.feature.explore

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cosmicwindow.R
import com.example.cosmicwindow.core.designsystem.components.AppHeader
import com.example.cosmicwindow.core.designsystem.components.ErrorBox
import com.example.cosmicwindow.core.designsystem.theme.Spacing

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

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        // todo refressh hook impl
        if (uiState.error != null) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                ErrorBox(text = uiState.error!!, onClick = { })
            }
            return@Scaffold
        }
//        if (uiState.images.isEmpty()) {
//            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
//                ErrorBox(text = "No Images found!")
//            }
//            return@Scaffold
//        }

        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .padding(horizontal = Spacing.s16, vertical = Spacing.s16)
                    .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.s16),
        ) {
            SearchBar()
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier) {
}
