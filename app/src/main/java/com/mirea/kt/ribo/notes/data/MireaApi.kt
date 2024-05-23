package com.mirea.kt.ribo.notes.data

import com.mirea.kt.ribo.notes.domain.Task
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface MireaApi {
    @FormUrlEncoded
    @POST("/coursework/login.php")
    fun getTask(@FieldMap params: Map<String, String>): Call<Task>

}
