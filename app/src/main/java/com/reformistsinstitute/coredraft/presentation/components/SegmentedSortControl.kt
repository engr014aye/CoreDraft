package com.reformistsinstitute.coredraft.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.reformistsinstitute.coredraft.domain.model.NoteSortOption
import com.reformistsinstitute.coredraft.presentation.theme.LocalGlassColors
import com.reformistsinstitute.coredraft.presentation.theme.SquircleInput
import com.reformistsinstitute.coredraft.presentation.theme.SquirclePill

@Composable
fun SegmentedSortControl(
    selectedOption: NoteSortOption,
    onOptionSelected: (NoteSortOption) -> Unit,
    modifier: Modifier = Modifier
) {
    val glassColors = LocalGlassColors.current

    val options = listOf(
        NoteSortOption.DATE_MODIFIED_DESC to "Recent",
        NoteSortOption.DATE_CREATED_DESC to "Newest",
        NoteSortOption.TITLE_ASC to "A-Z"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(38.dp)
            .clip(SquircleInput)
            .background(glassColors.searchBarBackground)
            .border(1.dp, glassColors.glassBorder, SquircleInput)
            .padding(3.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            options.forEach { (option, label) ->
                val isSelected = option == selectedOption
                val itemBg by animateColorAsState(
                    targetValue = if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    label = "segment_bg"
                )
                val textColor by animateColorAsState(
                    targetValue = if (isSelected) MaterialTheme.colorScheme.onSurface else glassColors.subtleText,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    label = "segment_text"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(32.dp)
                        .clip(SquircleInput)
                        .background(itemBg)
                        .springPressClickable { onOptionSelected(option) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        ),
                        color = textColor
                    )
                }
            }
        }
    }
}
