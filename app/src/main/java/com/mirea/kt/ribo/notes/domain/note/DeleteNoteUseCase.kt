package com.mirea.kt.ribo.notes.domain.note

import com.mirea.kt.ribo.notes.domain.NotesRepository

class DeleteNoteUseCase(private val notesRepository: NotesRepository) {
    fun deleteNote(id: Int) {
        notesRepository.deleteNote(id)
    }
}