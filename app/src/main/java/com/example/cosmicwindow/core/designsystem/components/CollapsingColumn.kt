package com.example.cosmicwindow.core.designsystem.components

import android.text.Layout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.layout
import com.example.cosmicwindow.core.designsystem.theme.Spacing
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingColumn(
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(Spacing.s8),
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable ColumnScope.() -> Unit,
) {
    var headerHeightPx by remember { mutableIntStateOf(0) }

    LaunchedEffect(headerHeightPx) {
        scrollBehavior.state.heightOffsetLimit = -headerHeightPx.toFloat()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clipToBounds()
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints.copy(minHeight = 0))
                headerHeightPx = placeable.height

                val offset = scrollBehavior.state.heightOffset
                val visibleHeight = (placeable.height + offset)
                    .roundToInt()
                    .coerceIn(0, placeable.height)

                layout(placeable.width, visibleHeight) {
                    placeable.placeRelative(0, offset.roundToInt())
                }
            },
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        content = content,
    )
}
