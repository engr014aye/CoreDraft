package com.reformistsinstitute.coredraft.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Continuous iOS-style squircle corner radii
val SquircleShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(26.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

val SquirclePill = RoundedCornerShape(50)
val SquircleCard = RoundedCornerShape(22.dp)
val SquircleDialog = RoundedCornerShape(28.dp)
val SquircleInput = RoundedCornerShape(16.dp)
