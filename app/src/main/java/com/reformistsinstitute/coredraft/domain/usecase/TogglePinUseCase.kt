package com.reformistsinstitute.coredraft.domain.usecase

import com.reformistsinstitute.coredraft.domain.repository.NoteRepository

class TogglePinUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(id: Long) {
        repository.togglePin(id)
    }
}
