package com.mirea.kt.ribo.notes.domain.notebook

import com.mirea.kt.ribo.notes.domain.NotesRepository

class GetNotebookListUseCase(private val notesRepository: NotesRepository) {
    fun getNotebookList(id: Int):List<NotebookItem>{
        return notesRepository.getNotebookList(id)
    }
}