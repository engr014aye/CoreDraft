package com.reformistsinstitute.coredraft.data.repository

import com.reformistsinstitute.coredraft.data.local.NoteDao
import com.reformistsinstitute.coredraft.data.local.toDomain
import com.reformistsinstitute.coredraft.data.local.toEntity
import com.reformistsinstitute.coredraft.domain.model.Note
import com.reformistsinstitute.coredraft.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepositoryImpl(
    private val noteDao: NoteDao
) : NoteRepository {

    override fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getNoteById(id: Long): Flow<Note?> {
        return noteDao.getNoteById(id).map { entity ->
            entity?.toDomain()
        }
    }

    override fun searchNotes(query: String): Flow<List<Note>> {
        return noteDao.searchNotes(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun upsertNote(note: Note): Long {
        return noteDao.upsertNote(note.toEntity())
    }

    override suspend fun deleteNote(id: Long) {
        noteDao.deleteById(id)
    }

    override suspend fun deleteNotes(ids: List<Long>) {
        noteDao.deleteByIds(ids)
    }

    override suspend fun togglePin(id: Long) {
        noteDao.togglePin(id, System.currentTimeMillis())
    }
}
