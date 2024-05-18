package com.mirea.kt.ribo.notes.domain.notebook

import android.graphics.Color
import com.mirea.kt.ribo.notes.domain.note.NoteItem

data class NotebookItem(
    val id: Int,
    val color: Color,
    val notes: List<NoteItem>
)
