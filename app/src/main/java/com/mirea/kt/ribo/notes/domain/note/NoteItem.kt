package com.mirea.kt.ribo.notes.domain.note

import android.graphics.Color

data class NoteItem(
    val id: Int,
    val title: String,
    val body: String,
    val image: Int,
    val remindTime: Int,
    val color: Color,
    val notebookId: Int
)
