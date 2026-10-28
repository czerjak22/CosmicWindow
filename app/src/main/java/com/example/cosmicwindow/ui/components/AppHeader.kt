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
import androidx.compose.ui.unit.dp


@Composable
fun AppHeader(
    modifier: Modifier = Modifier,
    leftIcon: Int,
    rightButtonIcon: Int,
    onClick: () -> Unit
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
                .weight(1f)
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(leftIcon),
                contentDescription = "Today Header's icon",
                modifier = Modifier
                    .size(48.dp),
                // contentScale = ContentScale.Fit
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
        IconButton(onClick = onClick, modifier = Modifier.padding(end = 16.dp)) {
            Icon(
                painterResource(rightButtonIcon),
                contentDescription = "Day set icon",
                tint = MaterialTheme.colorScheme.onSecondary,
            )
        }

    }

}