package com.mirea.kt.ribo.notes.data

import com.mirea.kt.ribo.notes.domain.Task
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MireaApi {
    @POST("/coursework/login.php")
    fun getTask(@Body body: BodyData): Call<Task>
}
