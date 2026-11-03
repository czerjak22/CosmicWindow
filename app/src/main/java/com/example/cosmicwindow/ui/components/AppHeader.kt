package com.example.cosmicwindow.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.cosmicwindow.ui.theme.Spacing


@Composable
fun AppHeader(
    modifier: Modifier = Modifier,
    mainTitle: String,
    secondaryTitle: String,
    leftIcon: Int,
    onClick: () -> Unit,
    leftIconModifier: Modifier = Modifier,
    leftIconSize: Dp = Spacing.s48,
    leftIconComposable: @Composable () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Row(
            Modifier
                .padding(horizontal = Spacing.s16)
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.s8)
        ) {
            Image(
                painter = painterResource(leftIcon),
                contentDescription = "Today Header's icon",
                modifier = leftIconModifier
                    .size(leftIconSize),
                // contentScale = ContentScale.Fit
            )
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    mainTitle,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    secondaryTitle,
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
        IconButton(onClick = onClick) {
            leftIconComposable()
        }

    }

}