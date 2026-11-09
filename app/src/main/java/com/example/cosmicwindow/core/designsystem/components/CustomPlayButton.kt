package com.example.cosmicwindow.core.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.cosmicwindow.R
import com.example.cosmicwindow.core.designsystem.theme.Spacing

@Composable
fun CustomPlayButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    @DrawableRes icon: Int = R.drawable.play_icon,
) {
    Box(
        modifier =
            modifier
                .size(Spacing.s56)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape,
                ).clickable(
                    onClick = onClick,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = "Play",
        )
    }
}
