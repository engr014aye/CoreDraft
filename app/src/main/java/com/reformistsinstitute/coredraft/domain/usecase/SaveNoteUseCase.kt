package com.reformistsinstitute.coredraft.domain.usecase

import com.reformistsinstitute.coredraft.domain.model.Note
import com.reformistsinstitute.coredraft.domain.repository.NoteRepository

class SaveNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(note: Note): Long {
        if (note.isBlank) {
            return -1L
        }
        val cleanNote = note.copy(
            title = note.title.trim(),
            content = note.content.trimEnd(),
            updatedAt = System.currentTimeMillis()
        )
        return repository.upsertNote(cleanNote)
    }
}
