package com.mirea.kt.ribo.notes.presentation

import androidx.lifecycle.ViewModel
import com.mirea.kt.ribo.notes.domain.GetStudentTaskUseCase
import com.mirea.kt.ribo.notes.domain.NotesRepository
import com.mirea.kt.ribo.notes.domain.Task
import com.mirea.kt.ribo.notes.domain.note.AddNoteUseCase
import com.mirea.kt.ribo.notes.domain.note.DeleteNoteUseCase
import com.mirea.kt.ribo.notes.domain.note.EditNoteUseCase
import com.mirea.kt.ribo.notes.domain.note.GetNoteUseCase
import com.mirea.kt.ribo.notes.domain.note.GetNotesListUseCase

class MainViewModel(repository: NotesRepository): ViewModel() {
    private val getStudentTaskUseCase =  GetStudentTaskUseCase(repository)

    private val addNoteUseCase = AddNoteUseCase(repository)
    private val deleteNoteUseCase = DeleteNoteUseCase(repository)
    private val editNoteUseCase = EditNoteUseCase(repository)
    private val getNoteListUseCase = GetNotesListUseCase(repository)
    private val getNoteUseCase = GetNoteUseCase(repository)




    suspend fun getStudentTask(login: String, password: String, studentGroup: String): Task {
        return getStudentTaskUseCase.getTask(login, password, studentGroup)
    }



}