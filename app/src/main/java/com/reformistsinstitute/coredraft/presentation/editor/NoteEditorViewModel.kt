package com.reformistsinstitute.coredraft.presentation.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.reformistsinstitute.coredraft.domain.model.Note
import com.reformistsinstitute.coredraft.domain.usecase.DeleteNoteUseCase
import com.reformistsinstitute.coredraft.domain.usecase.GetNoteByIdUseCase
import com.reformistsinstitute.coredraft.domain.usecase.SaveNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoteEditorViewModel(
    private val noteId: Long?,
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoteEditorUiState(noteId = noteId))
    val uiState: StateFlow<NoteEditorUiState> = _uiState.asStateFlow()

    private var initialNote: Note? = null

    init {
        if (noteId != null && noteId > 0L) {
            loadNote(noteId)
        }
    }

    private fun loadNote(id: Long) {
        viewModelScope.launch {
            val note = getNoteByIdUseCase(id).firstOrNull()
            if (note != null) {
                initialNote = note
                _uiState.update { current ->
                    current.copy(
                        noteId = note.id,
                        title = note.title,
                        content = note.content,
                        isPinned = note.isPinned,
                        color = note.color,
                        tags = note.tags,
                        wordCount = note.wordCount,
                        charCount = note.charCount,
                        lastSavedFormatted = formatTimestamp(note.updatedAt)
                    )
                }
            }
        }
    }

    fun onEvent(event: NoteEditorEvent) {
        when (event) {
            is NoteEditorEvent.OnTitleChange -> {
                _uiState.update { it.copy(title = event.title) }
            }
            is NoteEditorEvent.OnContentChange -> {
                val words = if (event.content.isBlank()) 0 else event.content.trim().split("\\s+".toRegex()).size
                _uiState.update {
                    it.copy(
                        content = event.content,
                        wordCount = words,
                        charCount = event.content.length
                    )
                }
            }
            is NoteEditorEvent.OnColorSelect -> {
                _uiState.update { it.copy(color = event.color) }
            }
            is NoteEditorEvent.OnTagInputChange -> {
                _uiState.update { it.copy(tagInput = event.tag) }
            }
            is NoteEditorEvent.OnAddTag -> {
                val rawTag = _uiState.value.tagInput.trim().replace("#", "").replace(",", "")
                if (rawTag.isNotBlank() && !_uiState.value.tags.contains(rawTag)) {
                    _uiState.update {
                        it.copy(
                            tags = it.tags + rawTag,
                            tagInput = "",
                            isTagInputVisible = false
                        )
                    }
                }
            }
            is NoteEditorEvent.OnRemoveTag -> {
                _uiState.update { current ->
                    current.copy(tags = current.tags.filterNot { it == event.tag })
                }
            }
            is NoteEditorEvent.OnTogglePin -> {
                _uiState.update { it.copy(isPinned = !it.isPinned) }
            }
            is NoteEditorEvent.OnToggleTagInput -> {
                _uiState.update { it.copy(isTagInputVisible = !it.isTagInputVisible) }
            }
            is NoteEditorEvent.OnToggleColorPicker -> {
                _uiState.update { it.copy(isColorPickerVisible = !it.isColorPickerVisible) }
            }
            is NoteEditorEvent.OnSaveNote -> {
                saveNote()
            }
            is NoteEditorEvent.OnDeleteNote -> {
                val id = _uiState.value.noteId
                if (id != null && id > 0L) {
                    viewModelScope.launch {
                        deleteNoteUseCase(id)
                    }
                }
            }
        }
    }

    fun saveNote(onSaved: (() -> Unit)? = null) {
        val current = _uiState.value
        if (current.title.isBlank() && current.content.isBlank()) {
            onSaved?.invoke()
            return
        }

        viewModelScope.launch {
            val noteToSave = Note(
                id = current.noteId ?: 0L,
                title = current.title,
                content = current.content,
                isPinned = current.isPinned,
                color = current.color,
                tags = current.tags,
                createdAt = initialNote?.createdAt ?: System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            val newId = saveNoteUseCase(noteToSave)
            if (current.noteId == null || current.noteId == 0L) {
                _uiState.update { it.copy(noteId = newId, isSaved = true) }
            }
            onSaved?.invoke()
        }
    }

    private fun formatTimestamp(time: Long): String {
        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        return sdf.format(Date(time))
    }

    companion object {
        fun provideFactory(
            noteId: Long?,
            getNoteByIdUseCase: GetNoteByIdUseCase,
            saveNoteUseCase: SaveNoteUseCase,
            deleteNoteUseCase: DeleteNoteUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NoteEditorViewModel(
                    noteId,
                    getNoteByIdUseCase,
                    saveNoteUseCase,
                    deleteNoteUseCase
                ) as T
            }
        }
    }
}
