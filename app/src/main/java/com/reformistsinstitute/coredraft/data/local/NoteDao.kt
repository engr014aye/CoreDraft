package com.reformistsinstitute.coredraft.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY isPinned DESC, updatedAt DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNoteById(id: Long): Flow<NoteEntity?>

    @Query(
        """
        SELECT * FROM notes 
        WHERE title LIKE '%' || :query || '%' 
           OR content LIKE '%' || :query || '%' 
           OR tagsString LIKE '%' || :query || '%'
        ORDER BY isPinned DESC, updatedAt DESC
        """
    )
    fun searchNotes(query: String): Flow<List<NoteEntity>>

    @Upsert
    suspend fun upsertNote(note: NoteEntity): Long

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM notes WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<Long>)

    @Query("UPDATE notes SET isPinned = NOT isPinned, updatedAt = :timestamp WHERE id = :id")
    suspend fun togglePin(id: Long, timestamp: Long)
}
