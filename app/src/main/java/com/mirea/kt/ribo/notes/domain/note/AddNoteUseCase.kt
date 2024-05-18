package com.mirea.kt.ribo.notes.domain.note

import com.mirea.kt.ribo.notes.domain.NotesRepository

class AddNoteUseCase(private val notesRepository: NotesRepository) {
    fun addNote(noteItem: NoteItem) {
        notesRepository.addNote(noteItem)
    }
}