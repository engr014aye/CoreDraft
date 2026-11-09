package com.reformistsinstitute.coredraft.domain.usecase

import com.reformistsinstitute.coredraft.domain.repository.NoteRepository

class DeleteNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(id: Long) {
        repository.deleteNote(id)
    }

    suspend operator fun invoke(ids: List<Long>) {
        repository.deleteNotes(ids)
    }
}
