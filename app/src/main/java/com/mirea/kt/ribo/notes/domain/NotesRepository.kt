package com.mirea.kt.ribo.notes.domain

import com.mirea.kt.ribo.notes.domain.notebook.NotebookItem

interface NotesRepository {
    fun getTask(login: String, password: String, studentGroup: String):List<String>
    fun getNotebookList(id: Int):List<NotebookItem>
    fun removeNotebook(id: Int)
}