package com.mirea.kt.ribo.notes.domain.note

import com.mirea.kt.ribo.notes.domain.NotesRepository

class GetNotesListUseCase(private val notesRepository: NotesRepository) {
    fun getNotesList():List<NoteItem>{
        return notesRepository.getNotesList()
    }
}