package com.example.cosmicwindow.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cosmicwindow.R
import com.example.cosmicwindow.ui.theme.RadiusSize
import com.example.cosmicwindow.ui.theme.Spacing

@Preview
@Composable
fun ErrorBox(
    modifier: Modifier = Modifier,
    text: String = "Unable to reach NASA archive. Check connection.",
    onClick: () -> Unit = {},
    @DrawableRes icon: Int? = R.drawable.no_internet
) {
    Box(
        modifier = modifier
            .border(
                border = BorderStroke(
                    width = Spacing.border,
                    color = MaterialTheme.colorScheme.error.copy(alpha = 0.3f),
                ), shape = RoundedCornerShape(RadiusSize.r16)
            )
            .background(
                MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (icon != null) {
                Image(
                    painterResource(icon),
                    contentDescription = "No internet icon",
                    modifier = Modifier.padding(top = 27.dp, bottom = 27.dp)
                )
            }
            Text(
                text,
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 18.sp,
                modifier = Modifier.weight(1f).basicMarquee(),
                textAlign = TextAlign.Center
            )


            Button(
                modifier = Modifier.padding(
                    vertical = Spacing.s12
                ),
                onClick = onClick,
                contentPadding = PaddingValues(horizontal = Spacing.s16, vertical = Spacing.s16),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.25f),
                    contentColor = MaterialTheme.colorScheme.onError
                ),

                ) {
                Icon(
                    painter = painterResource(R.drawable.reload_icon),
                    contentDescription = null,
                    modifier = Modifier.size(Spacing.s12)
                )
                Spacer(Modifier.width(Spacing.s6))
                Text(
                    "Retry",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.W500
                )

            }
        }
    }
}