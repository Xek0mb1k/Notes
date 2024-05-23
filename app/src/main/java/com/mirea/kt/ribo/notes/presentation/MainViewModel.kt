package com.mirea.kt.ribo.notes.presentation

import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.lifecycle.ViewModel
import com.mirea.kt.ribo.notes.domain.GetStudentTaskUseCase
import com.mirea.kt.ribo.notes.domain.NotesRepository
import com.mirea.kt.ribo.notes.domain.Task
import com.mirea.kt.ribo.notes.domain.note.AddNoteUseCase
import com.mirea.kt.ribo.notes.domain.note.DeleteNoteUseCase
import com.mirea.kt.ribo.notes.domain.note.EditNoteUseCase
import com.mirea.kt.ribo.notes.domain.note.GetNoteUseCase
import com.mirea.kt.ribo.notes.domain.note.GetNotesListUseCase
import com.mirea.kt.ribo.notes.domain.note.NoteItem
import com.mirea.kt.ribo.notes.domain.notebook.AddNotebookUseCase
import com.mirea.kt.ribo.notes.domain.notebook.DeleteNotebookUseCase
import com.mirea.kt.ribo.notes.domain.notebook.EditNotebookUseCase
import com.mirea.kt.ribo.notes.domain.notebook.GetNotebookListUseCase
import com.mirea.kt.ribo.notes.domain.notebook.GetNotesListFromNotebookUseCase
import com.mirea.kt.ribo.notes.domain.notebook.NotebookItem

class MainViewModel(repository: NotesRepository): ViewModel() {
    private val getStudentTaskUseCase =  GetStudentTaskUseCase(repository)

    private val addNoteUseCase = AddNoteUseCase(repository)
    private val deleteNoteUseCase = DeleteNoteUseCase(repository)
    private val editNoteUseCase = EditNoteUseCase(repository)
    private val getNoteListUseCase = GetNotesListUseCase(repository)
    private val getNoteUseCase = GetNoteUseCase(repository)

    private val addNotebookUseCase = AddNotebookUseCase(repository)
    private val deleteNotebookUseCase = DeleteNotebookUseCase(repository)
    private val editNotebookUseCase = EditNotebookUseCase(repository)
    private val getNotebookListUseCase = GetNotebookListUseCase(repository)
    private val getNotesListFromNotebookUseCase = GetNotesListFromNotebookUseCase(repository)


    suspend fun getStudentTask(login: String, password: String, studentGroup: String): Task {
        return getStudentTaskUseCase.getTask(login, password, studentGroup)
    }

    fun addNote(note: NoteItem) {
        addNoteUseCase.addNote(note)
    }

    fun deleteNote(noteId: Int){
        deleteNoteUseCase.deleteNote(noteId)
    }

    fun editNote(editedNote: NoteItem) {
        editNoteUseCase.editNote(editedNote)
    }

    fun getNoteList(): List<NoteItem> {
        return getNoteListUseCase.getNotesList()
    }

    fun getNote(noteId: Int): NoteItem {
        return getNoteUseCase.getNote(noteId)
    }

    fun addNotebook(notebook: NotebookItem) {
        addNotebookUseCase.addNotebook(notebook)
    }

    fun deleteNotebook(notebookId: Int) {
        deleteNotebookUseCase.deleteNotebook(notebookId)
    }

    fun editNotebook(editedNotebook: NotebookItem) {
        return editNotebookUseCase.editNotebook(editedNotebook)
    }

    fun getNotebookList(): List<NotebookItem> {
        return getNotebookListUseCase.getNotebookList()
    }

    fun getNotesListFromNotebook(notebookId: Int): List<NoteItem> {
        return getNotesListFromNotebookUseCase.getNotesListFromNotebook(notebookId)
    }

}