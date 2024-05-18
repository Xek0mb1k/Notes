package com.mirea.kt.ribo.notes.domain.note

import com.mirea.kt.ribo.notes.domain.NotesRepository

class EditNoteUseCase(private val notesRepository: NotesRepository) {
    fun editNote(noteItem: NoteItem) {
        notesRepository.editNote(noteItem)
    }
}