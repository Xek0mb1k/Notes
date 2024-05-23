package com.mirea.kt.ribo.notes.data

import android.content.Context
import android.util.Log
import com.mirea.kt.ribo.notes.domain.NotesRepository
import com.mirea.kt.ribo.notes.domain.Task
import com.mirea.kt.ribo.notes.domain.note.NoteItem
import com.mirea.kt.ribo.notes.domain.notebook.NotebookItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class NotesRepositoryImpl(private val context: Context) : NotesRepository {
    private val dbManager = DBManager(NoteDatabaseHelper(context))
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://android-for-students.ru/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val mireaApi: MireaApi = retrofit.create(MireaApi::class.java)

    override suspend fun getTask(login: String, password: String, studentGroup: String): Task {

        val params: MutableMap<String, String> = HashMap()
        params["lgn"] = login
        params["pwd"] = password
        params["g"] = studentGroup

        val call: Call<Task> = mireaApi.getTask(params)
        var task = Task(emptyList(), -1, "", "", -1)
        call.enqueue(object : Callback<Task> {
            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                if (response.isSuccessful) {
                    Log.d("DEBUG_DATABASE", "SUCCESSFUL ${response.body()}")
                    task = response.body()!!
                } else {
                    Log.d("DEBUG_DATABASE", "ERROR ${response.body()}")
                }
            }

            override fun onFailure(call: Call<Task>, t: Throwable) {
                Log.e("DEBUG_DATABASE", "Error sending after sending this data: $login $password")
            }
        })
        return task
    }

    override fun addNote(noteItem: NoteItem) {
        dbManager.addNote(noteItem)
    }

    override fun deleteNote(id: Int) {
        dbManager.deleteNote(id)
    }

    override fun editNote(noteItem: NoteItem) {
        dbManager.editNote(noteItem)
    }

    override fun getNote(id: Int): NoteItem {
        return dbManager.getNote(id)
    }

    override fun getNotesList(): List<NoteItem> {
        return dbManager.getAllNotesFromDatabase()
    }


    override fun addNotebook(notebookItem: NotebookItem) {
        dbManager.addNotebook(notebookItem)
    }

    override fun deleteNotebook(notebookId: Int) {
        dbManager.deleteNotebook(notebookId)
    }

    override fun editNotebook(notebookItem: NotebookItem) {
        dbManager.editNotebook(notebookItem)
    }

    override fun getNotebookList(): List<NotebookItem> {
        return dbManager.getNotebookList()
    }

    override fun getNotesListFromNotebook(id: Int): List<NoteItem> {
        return dbManager.getNotesListFromNotebook(id)
    }
}