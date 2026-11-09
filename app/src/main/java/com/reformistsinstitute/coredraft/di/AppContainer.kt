package com.reformistsinstitute.coredraft.di

import android.content.Context
import com.reformistsinstitute.coredraft.data.local.CoreDraftDatabase
import com.reformistsinstitute.coredraft.data.repository.NoteRepositoryImpl
import com.reformistsinstitute.coredraft.domain.repository.NoteRepository
import com.reformistsinstitute.coredraft.domain.usecase.DeleteNoteUseCase
import com.reformistsinstitute.coredraft.domain.usecase.GetNoteByIdUseCase
import com.reformistsinstitute.coredraft.domain.usecase.GetNotesUseCase
import com.reformistsinstitute.coredraft.domain.usecase.SaveNoteUseCase
import com.reformistsinstitute.coredraft.domain.usecase.SearchNotesUseCase
import com.reformistsinstitute.coredraft.domain.usecase.TogglePinUseCase

class AppContainer(context: Context) {
    private val database: CoreDraftDatabase by lazy {
        CoreDraftDatabase.getInstance(context)
    }

    val noteRepository: NoteRepository by lazy {
        NoteRepositoryImpl(database.noteDao)
    }

    val getNotesUseCase: GetNotesUseCase by lazy {
        GetNotesUseCase(noteRepository)
    }

    val getNoteByIdUseCase: GetNoteByIdUseCase by lazy {
        GetNoteByIdUseCase(noteRepository)
    }

    val saveNoteUseCase: SaveNoteUseCase by lazy {
        SaveNoteUseCase(noteRepository)
    }

    val deleteNoteUseCase: DeleteNoteUseCase by lazy {
        DeleteNoteUseCase(noteRepository)
    }

    val togglePinUseCase: TogglePinUseCase by lazy {
        TogglePinUseCase(noteRepository)
    }

    val searchNotesUseCase: SearchNotesUseCase by lazy {
        SearchNotesUseCase(noteRepository)
    }
}
