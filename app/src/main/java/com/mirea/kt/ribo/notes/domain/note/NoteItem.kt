package com.mirea.kt.ribo.notes.domain.note

import android.graphics.Bitmap

data class NoteItem(
    val id: Int = UNDEFINED_INT,
    val title: String = UNDEFINED_STRING,
    val body: String = UNDEFINED_STRING,
    val image: Bitmap? = null,
    val remindTime: Int = UNDEFINED_INT,
    val color: Int = UNDEFINED_INT,
    val notebookId: Int = UNDEFINED_INT

) {
    companion object {
        const val UNDEFINED_INT = -1
        const val UNDEFINED_STRING = ""

    }
}
