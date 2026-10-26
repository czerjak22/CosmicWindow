package com.example.cosmicwindow.feature.today

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cosmicwindow.R

@Composable
fun TodayScreen(modifier: Modifier = Modifier) {

    Column(modifier = modifier) {
        TodayHeader()
    }
}

@Preview
@Composable
fun TodayHeader(modifier: Modifier = Modifier) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.header_image),
                contentDescription = "Today Header's icon",

                modifier = Modifier.padding(start = 16.dp, end = 8.dp).size(32.dp),
                contentScale = ContentScale.Fit
            )
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    "Cosmic Window",
                    color = Color.Cyan,
                    style = MaterialTheme.typography.headlineLarge,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier.height(28.dp),
                )
                Text(
                    "ASTRONOMY PICTURE OF THE DAY",
                    color = Color.Cyan,
                    style = MaterialTheme.typography.labelSmall,
                    //textAlign = TextAlign.Center,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.W400,
                    modifier = Modifier.height(26.dp),

                )
            }
        }
        IconButton({}) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = "Day set icon",
                tint = Color.White,
            )
        }

    }
}
