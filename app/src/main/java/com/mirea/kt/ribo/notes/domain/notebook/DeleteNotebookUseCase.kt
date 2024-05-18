package com.mirea.kt.ribo.notes.domain.notebook

import com.mirea.kt.ribo.notes.domain.NotesRepository

class DeleteNotebookUseCase(private val notesRepository: NotesRepository) {
    fun deleteNotebook(notebookId: Int){
        notesRepository.deleteNotebook(notebookId)
    }
}