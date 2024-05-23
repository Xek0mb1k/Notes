package com.mirea.kt.ribo.notes.domain.note

import android.graphics.Bitmap

data class NoteItem(
    val id: Int,
    val title: String,
    val body: String,
    val image: Bitmap,
    val remindTime: Int,
    val color: Int,
    val notebookId: Int
)
