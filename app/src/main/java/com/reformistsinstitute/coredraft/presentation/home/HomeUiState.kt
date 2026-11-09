package com.reformistsinstitute.coredraft.presentation.home

import com.reformistsinstitute.coredraft.domain.model.Note
import com.reformistsinstitute.coredraft.domain.model.NoteSortOption

data class HomeUiState(
    val isLoading: Boolean = false,
    val notes: List<Note> = emptyList(),
    val pinnedNotes: List<Note> = emptyList(),
    val otherNotes: List<Note> = emptyList(),
    val searchQuery: String = "",
    val selectedSort: NoteSortOption = NoteSortOption.DATE_MODIFIED_DESC,
    val selectedTag: String? = null,
    val availableTags: List<String> = emptyList(),
    val recentlyDeletedNote: Note? = null
)
