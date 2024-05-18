package com.mirea.kt.ribo.notes.data

import android.util.Log
import com.mirea.kt.ribo.notes.domain.NotesRepository
import com.mirea.kt.ribo.notes.domain.note.NoteItem
import com.mirea.kt.ribo.notes.domain.notebook.NotebookItem
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NotesRepositoryImpl : NotesRepository {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://android-for-students.ru/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val mireaApi: MireaApi = retrofit.create(MireaApi::class.java)

    override fun getTask(login: String, password: String, studentGroup: String): List<String> {
        val task = mireaApi.getTask(login, password, studentGroup)
        if ("error" in task[0]) {
            Log.e("Request Error", "Error sending after sending this data: $login$password")
        }
        return task
    }

    override fun addNote(noteItem: NoteItem) {
        TODO("Not yet implemented")
    }

    override fun deleteNote(id: Int) {
        TODO("Not yet implemented")
    }

    override fun editNote(noteItem: NoteItem) {
        TODO("Not yet implemented")
    }

    override fun getNote(id: Int): NoteItem {
        TODO("Not yet implemented")
    }

    override fun getNotesList(): List<NoteItem> {
        TODO("Not yet implemented")
    }

    override fun addNotebook(notebookItem: NotebookItem) {
        TODO("Not yet implemented")
    }

    override fun deleteNotebook(notebookId: Int) {
        TODO("Not yet implemented")
    }

    override fun editNotebook(notebookItem: NotebookItem) {
        TODO("Not yet implemented")
    }

    override fun getNotebookList(): List<NotebookItem> {
        TODO("Not yet implemented")
    }

    override fun getNotesListFromNotebook(id: Int): List<NoteItem> {
        TODO("Not yet implemented")
    }
}