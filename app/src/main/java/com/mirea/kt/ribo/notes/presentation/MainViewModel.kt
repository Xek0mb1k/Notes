package com.mirea.kt.ribo.notes.presentation

import androidx.lifecycle.ViewModel
import com.mirea.kt.ribo.notes.domain.GetStudentTaskUseCase
import com.mirea.kt.ribo.notes.domain.NotesRepository

class MainViewModel(repository: NotesRepository): ViewModel() {
    private val getStudentTaskUseCase =  GetStudentTaskUseCase(repository)
    fun getStudentTask(login: String, password: String, studentGroup: String): List<String> {
        return getStudentTaskUseCase.getTask(login, password, studentGroup)
    }

}