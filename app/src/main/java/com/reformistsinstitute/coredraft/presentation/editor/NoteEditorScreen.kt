package com.reformistsinstitute.coredraft.presentation.editor

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Label
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ColorLens
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Label
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reformistsinstitute.coredraft.presentation.components.ColorPickerRow
import com.reformistsinstitute.coredraft.presentation.components.GlassCard
import com.reformistsinstitute.coredraft.presentation.components.springPressClickable
import com.reformistsinstitute.coredraft.presentation.theme.DestructiveRed
import com.reformistsinstitute.coredraft.presentation.theme.LocalGlassColors
import com.reformistsinstitute.coredraft.presentation.theme.SquircleCard
import com.reformistsinstitute.coredraft.presentation.theme.SquircleInput
import com.reformistsinstitute.coredraft.presentation.theme.SquirclePill

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NoteEditorScreen(
    viewModel: NoteEditorViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val glassColors = LocalGlassColors.current
    val isDark = isSystemInDarkTheme()

    val noteCanvasColor = if (isDark) {
        Color(uiState.color.darkHex).copy(alpha = 0.5f)
    } else {
        Color(uiState.color.lightHex).copy(alpha = 0.85f)
    }

    val handleBack = {
        viewModel.saveNote {
            onNavigateBack()
        }
    }

    BackHandler {
        handleBack()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            // iOS Frosted Floating Bottom Toolbar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = SquirclePill
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Word & Char Counter
                        Text(
                            text = "${uiState.wordCount} words • ${uiState.charCount} chars",
                            style = MaterialTheme.typography.labelSmall,
                            color = glassColors.subtleText
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Color Picker Toggle
                            IconButton(
                                onClick = { viewModel.onEvent(NoteEditorEvent.OnToggleColorPicker) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.ColorLens,
                                    contentDescription = "Palette",
                                    tint = if (uiState.isColorPickerVisible) MaterialTheme.colorScheme.primary else glassColors.subtleText,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            // Tag Toggle
                            IconButton(
                                onClick = { viewModel.onEvent(NoteEditorEvent.OnToggleTagInput) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.Label,
                                    contentDescription = "Tags",
                                    tint = if (uiState.isTagInputVisible) MaterialTheme.colorScheme.primary else glassColors.subtleText,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        val statusBarTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(noteCanvasColor)
        ) {
            // iOS Frosted Top Navigation Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = statusBarTop + 6.dp, start = 12.dp, end = 12.dp, bottom = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = handleBack,
                    modifier = Modifier
                        .size(40.dp)
                        .springPressClickable { handleBack() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Pin Button
                    IconButton(
                        onClick = { viewModel.onEvent(NoteEditorEvent.OnTogglePin) },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = if (uiState.isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                            contentDescription = if (uiState.isPinned) "Unpin" else "Pin",
                            tint = if (uiState.isPinned) MaterialTheme.colorScheme.primary else glassColors.subtleText
                        )
                    }

                    // Delete Button (if existing note)
                    if (uiState.noteId != null && uiState.noteId!! > 0L) {
                        IconButton(
                            onClick = {
                                viewModel.onEvent(NoteEditorEvent.OnDeleteNote)
                                onNavigateBack()
                            },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Delete,
                                contentDescription = "Delete",
                                tint = DestructiveRed
                            )
                        }
                    }

                    // Done / Save Pill
                    Box(
                        modifier = Modifier
                            .clip(SquirclePill)
                            .background(MaterialTheme.colorScheme.primary)
                            .springPressClickable { handleBack() }
                            .padding(horizontal = 16.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = "Done",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                }
            }

            // Expandable Color Picker Drawer
            AnimatedVisibility(
                visible = uiState.isColorPickerVisible,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = SquircleCard
                ) {
                    ColorPickerRow(
                        selectedColor = uiState.color,
                        onColorSelect = { viewModel.onEvent(NoteEditorEvent.OnColorSelect(it)) },
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }

            // Expandable Tag Adder Drawer
            AnimatedVisibility(
                visible = uiState.isTagInputVisible,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = SquircleCard
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .clip(SquircleInput)
                                .background(glassColors.searchBarBackground)
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (uiState.tagInput.isEmpty()) {
                                Text(
                                    text = "Tag name (e.g. Ideas, Project)",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = glassColors.subtleText
                                )
                            }
                            BasicTextField(
                                value = uiState.tagInput,
                                onValueChange = { viewModel.onEvent(NoteEditorEvent.OnTagInputChange(it)) },
                                singleLine = true,
                                textStyle = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onBackground
                                ),
                                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(
                                    onDone = { viewModel.onEvent(NoteEditorEvent.OnAddTag) }
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(
                            modifier = Modifier
                                .clip(SquirclePill)
                                .background(MaterialTheme.colorScheme.primary)
                                .springPressClickable { viewModel.onEvent(NoteEditorEvent.OnAddTag) }
                                .padding(horizontal = 14.dp, vertical = 9.dp)
                        ) {
                            Text(
                                text = "Add",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // Scrollable Note Content Canvas
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 22.dp, vertical = 12.dp)
            ) {
                // Existing Tags Flow
                if (uiState.tags.isNotEmpty()) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        uiState.tags.forEach { tag ->
                            Row(
                                modifier = Modifier
                                    .clip(SquirclePill)
                                    .background(Color(uiState.color.accentHex).copy(alpha = 0.15f))
                                    .border(
                                        1.dp,
                                        Color(uiState.color.accentHex).copy(alpha = 0.35f),
                                        SquirclePill
                                    )
                                    .padding(start = 10.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "#$tag",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = Color(uiState.color.accentHex)
                                )
                                IconButton(
                                    onClick = { viewModel.onEvent(NoteEditorEvent.OnRemoveTag(tag)) },
                                    modifier = Modifier.size(20.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Close,
                                        contentDescription = "Remove tag",
                                        tint = Color(uiState.color.accentHex),
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Title Input
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (uiState.title.isEmpty()) {
                        Text(
                            text = "Title",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = glassColors.subtleText.copy(alpha = 0.5f)
                            )
                        )
                    }

                    BasicTextField(
                        value = uiState.title,
                        onValueChange = { viewModel.onEvent(NoteEditorEvent.OnTitleChange(it)) },
                        textStyle = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Content Input (Infinite Canvas)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false),
                    contentAlignment = Alignment.TopStart
                ) {
                    if (uiState.content.isEmpty()) {
                        Text(
                            text = "Draft your ideas, outlines, and notes here...",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = glassColors.subtleText.copy(alpha = 0.5f),
                                lineHeight = 24.sp
                            )
                        )
                    }

                    BasicTextField(
                        value = uiState.content,
                        onValueChange = { viewModel.onEvent(NoteEditorEvent.OnContentChange(it)) },
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 24.sp
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                    )
                }

                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}
