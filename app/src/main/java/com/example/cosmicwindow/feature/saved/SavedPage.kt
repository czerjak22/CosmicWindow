package com.example.cosmicwindow.feature.saved

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.cosmicwindow.R
import com.example.cosmicwindow.core.designsystem.components.AppHeader
import com.example.cosmicwindow.core.designsystem.components.CollapsingColumn
import com.example.cosmicwindow.core.designsystem.theme.Spacing


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedPage(
    viewModel: SavedViewModel = hiltViewModel()
) {
    //  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehaviour.nestedScrollConnection),
    ) { innerPadding ->

//            if (uiState.error != null) {
//                Box(
//                    modifier =
//                        Modifier
//                            .padding(innerPadding)
//                            .padding(Spacing.s16)
//                            .fillMaxWidth(),
//                    contentAlignment = Alignment.Center,
//                ) {
//                    ErrorBox(text = uiState.error!!, onClick = viewModel::onErrorRetry)
//                }
//                return@Scaffold
//            }

        Column(
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CollapsingColumn(scrollBehavior = scrollBehaviour) {
                AppHeader(
                    leftIcon = R.drawable.earth_header_icon,
                    onClick = {},
                    leftIconSize = Spacing.s20,
                    rightIconComposable = {},
                    mainTitle = "Saved",
                    secondaryTitle = "COSMIC WINDOW",
                )
            }

            Box(Modifier.fillMaxSize(), Alignment.Center)
            {
                Text("Saved Page")
            }

        }
    }
}

