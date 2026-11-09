package com.reformistsinstitute.coredraft.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reformistsinstitute.coredraft.domain.model.Note
import com.reformistsinstitute.coredraft.presentation.components.GlassCard
import com.reformistsinstitute.coredraft.presentation.components.GlassSearchBar
import com.reformistsinstitute.coredraft.presentation.components.SegmentedSortControl
import com.reformistsinstitute.coredraft.presentation.components.SwipeToDeleteContainer
import com.reformistsinstitute.coredraft.presentation.components.TagChip
import com.reformistsinstitute.coredraft.presentation.components.springPressClickable
import com.reformistsinstitute.coredraft.presentation.theme.LocalGlassColors
import com.reformistsinstitute.coredraft.presentation.theme.SquircleCard
import com.reformistsinstitute.coredraft.presentation.theme.SquirclePill
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToEditor: (Long?) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val glassColors = LocalGlassColors.current

    LaunchedEffect(uiState.recentlyDeletedNote) {
        val deleted = uiState.recentlyDeletedNote
        if (deleted != null) {
            val result = snackbarHostState.showSnackbar(
                message = "Draft deleted",
                actionLabel = "Undo",
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.onEvent(HomeEvent.OnUndoDelete)
            } else {
                viewModel.onEvent(HomeEvent.OnClearRecentlyDeleted)
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigateToEditor(null) },
                shape = SquirclePill,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier
                    .padding(bottom = 8.dp, end = 4.dp)
                    .springPressClickable { onNavigateToEditor(null) }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "New Draft",
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "New Draft",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }
        }
    ) { innerPadding ->
        val statusBarTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // iOS Style Top Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = statusBarTop + 8.dp, start = 20.dp, end = 20.dp, bottom = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "CoreDraft",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = if (uiState.notes.isEmpty()) "Capture your thoughts" else "${uiState.notes.size} drafts saved",
                            style = MaterialTheme.typography.bodyMedium,
                            color = glassColors.subtleText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Search Bar
                GlassSearchBar(
                    query = uiState.searchQuery,
                    onQueryChange = { viewModel.onEvent(HomeEvent.OnSearchQueryChange(it)) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Segmented Sort Control
                SegmentedSortControl(
                    selectedOption = uiState.selectedSort,
                    onOptionSelected = { viewModel.onEvent(HomeEvent.OnSortOptionChange(it)) }
                )

                // Tag Filter Row (if tags exist)
                if (uiState.availableTags.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            TagChip(
                                tag = "All",
                                isSelected = uiState.selectedTag == null,
                                onClick = { viewModel.onEvent(HomeEvent.OnTagSelect(null)) }
                            )
                        }
                        items(uiState.availableTags) { tag ->
                            TagChip(
                                tag = "#$tag",
                                isSelected = uiState.selectedTag == tag,
                                onClick = { viewModel.onEvent(HomeEvent.OnTagSelect(tag)) }
                            )
                        }
                    }
                }
            }

            // Main Content: Staggered Grid of Notes or Empty State
            if (uiState.notes.isEmpty() && !uiState.isLoading) {
                EmptyStateView(
                    isSearching = uiState.searchQuery.isNotBlank() || uiState.selectedTag != null,
                    onNewDraftClick = { onNavigateToEditor(null) }
                )
            } else {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 96.dp, top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalItemSpacing = 12.dp
                ) {
                    if (uiState.pinnedNotes.isNotEmpty()) {
                        item(span = StaggeredGridItemSpan.FullLine) {
                            SectionHeader(title = "PINNED", count = uiState.pinnedNotes.size)
                        }
                        items(uiState.pinnedNotes, key = { it.id }) { note ->
                            SwipeToDeleteContainer(
                                itemKey = note.id,
                                onDelete = { viewModel.onEvent(HomeEvent.OnDeleteNote(note)) }
                            ) {
                                NoteCard(
                                    note = note,
                                    onClick = { onNavigateToEditor(note.id) },
                                    onTogglePin = { viewModel.onEvent(HomeEvent.OnTogglePin(note.id)) }
                                )
                            }
                        }
                    }

                    if (uiState.otherNotes.isNotEmpty()) {
                        if (uiState.pinnedNotes.isNotEmpty()) {
                            item(span = StaggeredGridItemSpan.FullLine) {
                                SectionHeader(title = "NOTES", count = uiState.otherNotes.size)
                            }
                        }
                        items(uiState.otherNotes, key = { it.id }) { note ->
                            SwipeToDeleteContainer(
                                itemKey = note.id,
                                onDelete = { viewModel.onEvent(HomeEvent.OnDeleteNote(note)) }
                            ) {
                                NoteCard(
                                    note = note,
                                    onClick = { onNavigateToEditor(note.id) },
                                    onTogglePin = { viewModel.onEvent(HomeEvent.OnTogglePin(note.id)) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$title ($count)",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            ),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun NoteCard(
    note: Note,
    onClick: () -> Unit,
    onTogglePin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val noteBg = if (isDark) {
        Color(note.color.darkHex).copy(alpha = 0.82f)
    } else {
        Color(note.color.lightHex).copy(alpha = 0.92f)
    }

    val glassColors = LocalGlassColors.current
    val formattedDate = remember(note.updatedAt) {
        val sdf = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        sdf.format(Date(note.updatedAt))
    }

    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .springPressClickable(onClick = onClick),
        backgroundColor = noteBg,
        shape = SquircleCard
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = if (note.title.isBlank()) "Untitled Draft" else note.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = onTogglePin,
                    modifier = Modifier
                        .size(28.dp)
                        .padding(start = 4.dp)
                ) {
                    Icon(
                        imageVector = if (note.isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                        contentDescription = if (note.isPinned) "Unpin" else "Pin",
                        tint = if (note.isPinned) MaterialTheme.colorScheme.primary else glassColors.subtleText.copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            if (note.content.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = note.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f),
                    maxLines = 6,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 19.sp
                )
            }

            // Tags (if any)
            if (note.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    note.tags.take(2).forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(SquirclePill)
                                .background(Color(note.color.accentHex).copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "#$tag",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(note.color.accentHex),
                                maxLines = 1
                            )
                        }
                    }
                    if (note.tags.size > 2) {
                        Text(
                            text = "+${note.tags.size - 2}",
                            style = MaterialTheme.typography.labelSmall,
                            color = glassColors.subtleText,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Footer: Date & Word Count
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.labelSmall,
                    color = glassColors.subtleText
                )

                if (note.wordCount > 0) {
                    Text(
                        text = "${note.wordCount} words",
                        style = MaterialTheme.typography.labelSmall,
                        color = glassColors.subtleText
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyStateView(
    isSearching: Boolean,
    onNewDraftClick: () -> Unit
) {
    val glassColors = LocalGlassColors.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(glassColors.searchBarBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.EditNote,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(42.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (isSearching) "No matching drafts" else "No drafts yet",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (isSearching) {
                    "Try adjusting your search terms or clearing the tag filter."
                } else {
                    "Every great project starts with an outline.\nTap below to draft your first idea."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = glassColors.subtleText,
                lineHeight = 20.sp
            )

            if (!isSearching) {
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .clip(SquirclePill)
                        .background(MaterialTheme.colorScheme.primary)
                        .springPressClickable(onClick = onNewDraftClick)
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = "Create First Draft",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}
