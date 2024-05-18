package com.mirea.kt.ribo.notes.domain.notebook

import com.mirea.kt.ribo.notes.domain.NotesRepository
import com.mirea.kt.ribo.notes.domain.note.NoteItem

class GetNotesListFromNotebookUseCase(private val notesRepository: NotesRepository) {
    fun getNotesListFromNotebook(notebookId: Int):List<NoteItem>{
        return notesRepository.getNotesListFromNotebook(notebookId)
    }
}