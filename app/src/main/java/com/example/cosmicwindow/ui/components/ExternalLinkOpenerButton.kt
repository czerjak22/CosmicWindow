package com.example.cosmicwindow.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import com.example.cosmicwindow.R
import com.example.cosmicwindow.ui.theme.RadiusSize
import com.example.cosmicwindow.ui.theme.Spacing


@Composable
fun ExternalLinkOpenerButton(
    videoHandler: UriHandler,
    url: String,
    modifier: Modifier = Modifier,
    buttonText: String = "Open video in browser",
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    @DrawableRes icon: Int = R.drawable.open_elsewhere_icon,
) {
    Button(
        onClick = { videoHandler.openUri(url) },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        border = BorderStroke(
            Spacing.border,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
        ),
        shape = RoundedCornerShape(RadiusSize.r12)
    ) {
        Text(
            text = buttonText,
            style = textStyle,
            modifier = Modifier.padding(end = Spacing.s4)
        )
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(Spacing.s12)
        )
    }
}