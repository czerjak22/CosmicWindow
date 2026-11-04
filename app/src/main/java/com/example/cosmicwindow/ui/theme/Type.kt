package com.example.cosmicwindow.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.cosmicwindow.R

val SpaceGrotesk =
    FontFamily(
        Font(R.font.spacegrotesk_normal, FontWeight.Normal),
        Font(R.font.spacegrotesk_medium, FontWeight.Medium),
        Font(R.font.spacegrotesk_bold, FontWeight.Bold),
    )

val Inter =
    FontFamily(
        Font(R.font.inter_normal, FontWeight.Normal),
        Font(R.font.inter_medium, FontWeight.Medium),
        Font(R.font.inter_bold, FontWeight.Bold),
    )
val liberationMono =
    FontFamily(
        Font(R.font.liberationmono_regular, FontWeight.Normal),
        Font(R.font.liberationmono_bold_italic, FontWeight.Medium),
        Font(R.font.liberationmono_bold, FontWeight.Bold),
    )

val Typography =
    Typography(
        titleLarge =
            TextStyle(
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.W600,
                fontSize = 30.sp,
                lineHeight = 38.sp,
                letterSpacing = (-0.75).sp,
            ),
        headlineLarge =
            TextStyle(
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.W600,
                fontSize = 24.sp,
                lineHeight = 30.sp,
                letterSpacing = (-0.6).sp,
            ),
        headlineMedium =
            TextStyle(
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.W500,
                fontSize = 20.sp,
                lineHeight = 28.sp,
                letterSpacing = (-0.5).sp,
            ),
        headlineSmall =
            TextStyle(
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.W500,
                fontSize = 18.sp,
                lineHeight = 26.sp,
            ),
        // Inter: body text
        bodyLarge =
            TextStyle(
                fontFamily = Inter,
                fontWeight = FontWeight.W400,
                fontSize = 16.sp,
                lineHeight = 26.sp,
            ),
        bodyMedium =
            TextStyle(
                fontFamily = Inter,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            ),
        bodySmall =
            TextStyle(
                fontFamily = Inter,
                fontWeight = FontWeight.W400,
                fontSize = 12.sp,
                lineHeight = 16.sp,
            ),
        //    labelLarge = TextStyle(
//        fontFamily = liberationMono,
//        fontWeight = FontWeight.W400,
//        fontSize = 12.sp,
//        lineHeight = 16.sp
//    ),
//    labelMedium = TextStyle(
//        fontFamily = liberationMono,
//        fontWeight = FontWeight.W400,
//        fontSize = 12.sp,
//        lineHeight = 16.sp
//    ),
        labelSmall =
            TextStyle(
                fontFamily = liberationMono,
                fontWeight = FontWeight.W500,
                fontSize = 11.sp,
                lineHeight = 16.sp,
            ),
    )
