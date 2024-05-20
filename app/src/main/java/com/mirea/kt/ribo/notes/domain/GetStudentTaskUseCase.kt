package com.mirea.kt.ribo.notes.domain

class GetStudentTaskUseCase(private val notesRepository: NotesRepository) {
    suspend fun getTask(login: String, password: String, studentGroup: String):Task{
        return notesRepository.getTask(login, password, studentGroup)
    }
}