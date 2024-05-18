package com.mirea.kt.ribo.notes.domain

import com.mirea.kt.ribo.notes.domain.note.NoteItem
import com.mirea.kt.ribo.notes.domain.notebook.NotebookItem

interface NotesRepository {
    fun getTask(login: String, password: String, studentGroup: String): List<String>


    fun addNote(noteItem: NoteItem)
    fun deleteNote(id: Int)
    fun editNote(noteItem: NoteItem)
    fun getNote(id: Int): NoteItem
    fun getNotesList(): List<NoteItem>


    fun addNotebook(notebookItem: NotebookItem)
    fun deleteNotebook(notebookId: Int)
    fun editNotebook(notebookItem: NotebookItem)
    fun getNotebookList(): List<NotebookItem>
    fun getNotesListFromNotebook(id: Int): List<NoteItem>
}