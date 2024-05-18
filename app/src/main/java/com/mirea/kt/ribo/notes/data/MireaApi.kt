package com.mirea.kt.ribo.notes.data

import retrofit2.http.Body
import retrofit2.http.POST

interface MireaApi {
    @POST("/coursework/login.php")
    fun getTask(
        @Body lgn: String,
        @Body pwd: String,
        @Body g: String
    ): List<String>
}
