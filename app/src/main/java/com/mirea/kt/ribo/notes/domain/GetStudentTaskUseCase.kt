package com.mirea.kt.ribo.notes.domain

class GetStudentTaskUseCase(private val notesRepository: NotesRepository) {
    fun getTask(login: String, password: String, studentGroup: String):List<String>{
        return notesRepository.getTask(login, password, studentGroup)
    }
}