package com.reformistsinstitute.coredraft.presentation.editor

import com.reformistsinstitute.coredraft.domain.model.NoteColor

sealed interface NoteEditorEvent {
    data class OnTitleChange(val title: String) : NoteEditorEvent
    data class OnContentChange(val content: String) : NoteEditorEvent
    data class OnColorSelect(val color: NoteColor) : NoteEditorEvent
    data class OnTagInputChange(val tag: String) : NoteEditorEvent
    object OnAddTag : NoteEditorEvent
    data class OnRemoveTag(val tag: String) : NoteEditorEvent
    object OnTogglePin : NoteEditorEvent
    object OnToggleTagInput : NoteEditorEvent
    object OnToggleColorPicker : NoteEditorEvent
    object OnSaveNote : NoteEditorEvent
    object OnDeleteNote : NoteEditorEvent
}
