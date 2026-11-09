package com.reformistsinstitute.coredraft.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.reformistsinstitute.coredraft.presentation.theme.LocalGlassColors
import com.reformistsinstitute.coredraft.presentation.theme.SquircleCard

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color? = null,
    borderWidth: Dp = 1.dp,
    shape: Shape = SquircleCard,
    content: @Composable BoxScope.() -> Unit
) {
    val glassColors = LocalGlassColors.current
    val effectiveBg = backgroundColor ?: glassColors.glassBackground

    val borderBrush = Brush.verticalGradient(
        colors = listOf(
            glassColors.glassHighlight,
            glassColors.glassBorder
        )
    )

    Box(
        modifier = modifier
            .clip(shape)
            .background(effectiveBg)
            .border(
                BorderStroke(borderWidth, borderBrush),
                shape = shape
            ),
        content = content
    )
}
