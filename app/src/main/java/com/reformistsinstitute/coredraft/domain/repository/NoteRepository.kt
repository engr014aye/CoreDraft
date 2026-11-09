package com.reformistsinstitute.coredraft.domain.repository

import com.reformistsinstitute.coredraft.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    fun getNoteById(id: Long): Flow<Note?>
    fun searchNotes(query: String): Flow<List<Note>>
    suspend fun upsertNote(note: Note): Long
    suspend fun deleteNote(id: Long)
    suspend fun deleteNotes(ids: List<Long>)
    suspend fun togglePin(id: Long)
}
