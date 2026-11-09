package com.reformistsinstitute.coredraft.domain.model

data class Note(
    val id: Long = 0L,
    val title: String,
    val content: String,
    val isPinned: Boolean = false,
    val color: NoteColor = NoteColor.DEFAULT,
    val tags: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val wordCount: Int
        get() = if (content.isBlank()) 0 else content.trim().split("\\s+".toRegex()).size

    val charCount: Int
        get() = content.length

    val isBlank: Boolean
        get() = title.isBlank() && content.isBlank()
}
