package com.reformistsinstitute.coredraft.presentation.home

import com.reformistsinstitute.coredraft.domain.model.Note
import com.reformistsinstitute.coredraft.domain.model.NoteSortOption

sealed interface HomeEvent {
    data class OnSearchQueryChange(val query: String) : HomeEvent
    data class OnSortOptionChange(val sortOption: NoteSortOption) : HomeEvent
    data class OnTagSelect(val tag: String?) : HomeEvent
    data class OnDeleteNote(val note: Note) : HomeEvent
    data class OnTogglePin(val id: Long) : HomeEvent
    object OnUndoDelete : HomeEvent
    object OnClearRecentlyDeleted : HomeEvent
}
