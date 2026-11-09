package com.reformistsinstitute.coredraft.domain.usecase

import com.reformistsinstitute.coredraft.domain.model.Note
import com.reformistsinstitute.coredraft.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetNoteByIdUseCase(private val repository: NoteRepository) {
    operator fun invoke(id: Long): Flow<Note?> {
        return repository.getNoteById(id)
    }
}
