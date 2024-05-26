package com.mirea.kt.ribo.notes.presentation

import androidx.recyclerview.widget.DiffUtil
import com.mirea.kt.ribo.notes.domain.note.NoteItem

class NoteItemDiffCallBack : DiffUtil.ItemCallback<NoteItem>() {
    override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
        return oldItem == newItem
    }


}