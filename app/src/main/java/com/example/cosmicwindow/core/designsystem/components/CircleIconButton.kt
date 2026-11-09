package com.example.cosmicwindow.core.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.example.cosmicwindow.core.designsystem.theme.Spacing

@Composable
fun CircleIconButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    description: String?,
    onClick: () -> Unit,
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    foregroundColor: Color = MaterialTheme.colorScheme.onSecondary,
    size: Dp = Spacing.s48,
    iconSize: Dp = Spacing.s20,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(size),
        colors =
            IconButtonDefaults.iconButtonColors(
                containerColor = backgroundColor,
                contentColor = foregroundColor,
            ),
    ) {
        Icon(
            painterResource(icon),
            contentDescription = description,
            modifier = Modifier.size(iconSize),
        )
    }
}
