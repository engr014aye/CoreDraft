package com.reformistsinstitute.coredraft.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.reformistsinstitute.coredraft.domain.model.Note
import com.reformistsinstitute.coredraft.domain.model.NoteColor

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val content: String,
    val isPinned: Boolean,
    val colorId: String,
    val tagsString: String,
    val createdAt: Long,
    val updatedAt: Long
)

fun NoteEntity.toDomain(): Note {
    val tagList = if (tagsString.isBlank()) {
        emptyList()
    } else {
        tagsString.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    }
    return Note(
        id = id,
        title = title,
        content = content,
        isPinned = isPinned,
        color = NoteColor.fromId(colorId),
        tags = tagList,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Note.toEntity(): NoteEntity {
    val cleanTags = tags.map { it.trim().replace(",", "") }.filter { it.isNotEmpty() }.joinToString(",")
    return NoteEntity(
        id = id,
        title = title,
        content = content,
        isPinned = isPinned,
        colorId = color.id,
        tagsString = cleanTags,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
