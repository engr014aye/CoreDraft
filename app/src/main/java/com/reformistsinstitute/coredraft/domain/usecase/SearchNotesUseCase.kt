package com.reformistsinstitute.coredraft.domain.usecase

import com.reformistsinstitute.coredraft.domain.model.Note
import com.reformistsinstitute.coredraft.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchNotesUseCase(private val repository: NoteRepository) {
    operator fun invoke(query: String): Flow<List<Note>> {
        val trimmed = query.trim()
        return if (trimmed.isBlank()) {
            repository.getAllNotes().map { list ->
                list.sortedWith(compareByDescending<Note> { it.isPinned }.thenByDescending { it.updatedAt })
            }
        } else {
            repository.searchNotes(trimmed).map { list ->
                list.sortedWith(compareByDescending<Note> { it.isPinned }.thenByDescending { it.updatedAt })
            }
        }
    }
}
