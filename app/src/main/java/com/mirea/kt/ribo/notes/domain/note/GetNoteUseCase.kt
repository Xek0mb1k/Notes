package com.mirea.kt.ribo.notes.domain.note

import com.mirea.kt.ribo.notes.domain.NotesRepository

class GetNoteUseCase(private val notesRepository: NotesRepository) {
    fun getNote(id: Int): NoteItem {
        return notesRepository.getNote(id)
    }
}