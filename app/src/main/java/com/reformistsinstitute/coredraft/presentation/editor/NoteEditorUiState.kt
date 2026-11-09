package com.reformistsinstitute.coredraft.presentation.editor

import com.reformistsinstitute.coredraft.domain.model.NoteColor

data class NoteEditorUiState(
    val noteId: Long? = null,
    val title: String = "",
    val content: String = "",
    val isPinned: Boolean = false,
    val color: NoteColor = NoteColor.DEFAULT,
    val tags: List<String> = emptyList(),
    val tagInput: String = "",
    val isTagInputVisible: Boolean = false,
    val isColorPickerVisible: Boolean = false,
    val isSaved: Boolean = false,
    val wordCount: Int = 0,
    val charCount: Int = 0,
    val lastSavedFormatted: String = ""
)
