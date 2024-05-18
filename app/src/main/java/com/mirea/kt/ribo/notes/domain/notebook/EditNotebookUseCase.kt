package com.mirea.kt.ribo.notes.domain.notebook

import com.mirea.kt.ribo.notes.domain.NotesRepository

class EditNotebookUseCase(private val notesRepository: NotesRepository) {
    fun editNotebook(notebookItem: NotebookItem){
        notesRepository.editNotebook(notebookItem)
    }
}