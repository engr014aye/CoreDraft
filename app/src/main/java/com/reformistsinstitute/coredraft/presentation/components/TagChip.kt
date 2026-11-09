package com.reformistsinstitute.coredraft.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.reformistsinstitute.coredraft.presentation.theme.LocalGlassColors
import com.reformistsinstitute.coredraft.presentation.theme.SquirclePill

@Composable
fun TagChip(
    tag: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val glassColors = LocalGlassColors.current

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else glassColors.chipBackground,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "tag_bg"
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "tag_text"
    )

    Box(
        modifier = modifier
            .clip(SquirclePill)
            .background(backgroundColor)
            .border(
                1.dp,
                if (isSelected) MaterialTheme.colorScheme.primary else glassColors.glassBorder,
                SquirclePill
            )
            .springPressClickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Text(
            text = tag,
            style = MaterialTheme.typography.labelMedium,
            color = textColor
        )
    }
}
