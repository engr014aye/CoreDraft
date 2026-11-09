package com.reformistsinstitute.coredraft.domain.usecase

import com.reformistsinstitute.coredraft.domain.model.Note
import com.reformistsinstitute.coredraft.domain.model.NoteSortOption
import com.reformistsinstitute.coredraft.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotesUseCase(private val repository: NoteRepository) {
    operator fun invoke(
        sortOption: NoteSortOption = NoteSortOption.DATE_MODIFIED_DESC,
        tagFilter: String? = null
    ): Flow<List<Note>> {
        return repository.getAllNotes().map { list ->
            val filtered = if (tagFilter.isNullOrBlank()) {
                list
            } else {
                list.filter { note -> note.tags.any { it.equals(tagFilter, ignoreCase = true) } }
            }

            when (sortOption) {
                NoteSortOption.DATE_MODIFIED_DESC -> filtered.sortedWith(
                    compareByDescending<Note> { it.isPinned }.thenByDescending { it.updatedAt }
                )
                NoteSortOption.DATE_MODIFIED_ASC -> filtered.sortedWith(
                    compareByDescending<Note> { it.isPinned }.thenBy { it.updatedAt }
                )
                NoteSortOption.DATE_CREATED_DESC -> filtered.sortedWith(
                    compareByDescending<Note> { it.isPinned }.thenByDescending { it.createdAt }
                )
                NoteSortOption.TITLE_ASC -> filtered.sortedWith(
                    compareByDescending<Note> { it.isPinned }.thenBy(String.CASE_INSENSITIVE_ORDER) { it.title }
                )
            }
        }
    }
}
