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
    private val dbHelper = NoteDatabaseHelper(context)
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
        var task = Task(emptyList (), -1, "", "", -1)
        call.enqueue(object : Callback<Task> {
            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                if (response.isSuccessful) {
                    Log.d("DEBUG_TASK", "SUCCESSFUL ${response.body()}")
                    task = response.body()!!
                    // Обработка результата
                } else {
                    Log.d("DEBUG_TASK", "ERROR ${response.body()}")
                    TODO("implement error handling")

                }
            }

            override fun onFailure(call: Call<Task>, t: Throwable) {
                Log.e("DEBUG_TASK", "Error sending after sending this data: $login $password")
                TODO("implement error handling")
            }
        })
        return task
    }

    override fun addNote(noteItem: NoteItem) {
        dbHelper.addNote(
            noteItem
        )
        TODO("IMPLEMENT MORE OBJECTS (Image, Timer and other)")
    }

    override fun deleteNote(id: Int) {
        dbHelper.deleteNote(id)
    }

    override fun editNote(noteItem: NoteItem) {
        dbHelper.editNote(noteItem)
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