package com.reformistsinstitute.coredraft.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.reformistsinstitute.coredraft.domain.model.Note
import com.reformistsinstitute.coredraft.domain.model.NoteSortOption
import com.reformistsinstitute.coredraft.domain.usecase.DeleteNoteUseCase
import com.reformistsinstitute.coredraft.domain.usecase.GetNotesUseCase
import com.reformistsinstitute.coredraft.domain.usecase.SaveNoteUseCase
import com.reformistsinstitute.coredraft.domain.usecase.SearchNotesUseCase
import com.reformistsinstitute.coredraft.domain.usecase.TogglePinUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val getNotesUseCase: GetNotesUseCase,
    private val searchNotesUseCase: SearchNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val togglePinUseCase: TogglePinUseCase,
    private val saveNoteUseCase: SaveNoteUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedSort = MutableStateFlow(NoteSortOption.DATE_MODIFIED_DESC)
    private val _selectedTag = MutableStateFlow<String?>(null)
    private val _recentlyDeleted = MutableStateFlow<Note?>(null)

    val uiState: StateFlow<HomeUiState> = combine(
        _searchQuery,
        _selectedSort,
        _selectedTag,
        _recentlyDeleted
    ) { query, sort, tag, deleted ->
        FilterParams(query, sort, tag, deleted)
    }.flatMapLatest { params ->
        val notesFlow = if (params.query.isNotBlank()) {
            searchNotesUseCase(params.query)
        } else {
            getNotesUseCase(params.sort, params.tag)
        }

        combine(notesFlow, getNotesUseCase()) { notes, allNotes ->
            val allTags = allNotes
                .flatMap { it.tags }
                .distinct()
                .sorted()

            val filteredNotes = if (params.query.isNotBlank() && params.tag != null) {
                notes.filter { n -> n.tags.any { it.equals(params.tag, ignoreCase = true) } }
            } else {
                notes
            }

            HomeUiState(
                isLoading = false,
                notes = filteredNotes,
                pinnedNotes = filteredNotes.filter { it.isPinned },
                otherNotes = filteredNotes.filter { !it.isPinned },
                searchQuery = params.query,
                selectedSort = params.sort,
                selectedTag = params.tag,
                availableTags = allTags,
                recentlyDeletedNote = params.recentlyDeleted
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState(isLoading = true)
    )

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnSearchQueryChange -> {
                _searchQuery.value = event.query
            }
            is HomeEvent.OnSortOptionChange -> {
                _selectedSort.value = event.sortOption
            }
            is HomeEvent.OnTagSelect -> {
                _selectedTag.value = if (_selectedTag.value == event.tag) null else event.tag
            }
            is HomeEvent.OnDeleteNote -> {
                _recentlyDeleted.value = event.note
                viewModelScope.launch {
                    deleteNoteUseCase(event.note.id)
                }
            }
            is HomeEvent.OnTogglePin -> {
                viewModelScope.launch {
                    togglePinUseCase(event.id)
                }
            }
            is HomeEvent.OnUndoDelete -> {
                val toRestore = _recentlyDeleted.value ?: return
                _recentlyDeleted.value = null
                viewModelScope.launch {
                    saveNoteUseCase(toRestore)
                }
            }
            is HomeEvent.OnClearRecentlyDeleted -> {
                _recentlyDeleted.value = null
            }
        }
    }

    private data class FilterParams(
        val query: String,
        val sort: NoteSortOption,
        val tag: String?,
        val recentlyDeleted: Note?
    )

    companion object {
        fun provideFactory(
            getNotesUseCase: GetNotesUseCase,
            searchNotesUseCase: SearchNotesUseCase,
            deleteNoteUseCase: DeleteNoteUseCase,
            togglePinUseCase: TogglePinUseCase,
            saveNoteUseCase: SaveNoteUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(
                    getNotesUseCase,
                    searchNotesUseCase,
                    deleteNoteUseCase,
                    togglePinUseCase,
                    saveNoteUseCase
                ) as T
            }
        }
    }
}
