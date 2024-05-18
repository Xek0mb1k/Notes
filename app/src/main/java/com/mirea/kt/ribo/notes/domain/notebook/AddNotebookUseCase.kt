package com.mirea.kt.ribo.notes.domain.notebook

import com.mirea.kt.ribo.notes.domain.NotesRepository

class AddNotebookUseCase(private val notesRepository: NotesRepository) {
    fun addNotebook(notebookItem: NotebookItem){
        notesRepository.addNotebook(notebookItem)
    }
}