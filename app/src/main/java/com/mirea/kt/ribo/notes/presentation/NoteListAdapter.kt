package com.mirea.kt.ribo.notes.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.mirea.kt.ribo.notes.R
import com.mirea.kt.ribo.notes.domain.note.NoteItem

class NoteListAdapter : ListAdapter<NoteItem, NoteItemViewModel>(NoteItemDiffCallBack()) {


    var onNoteItemLongClickListener: ((NoteItem) -> Unit)? = null
    var onNoteItemClickListener: ((NoteItem) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteItemViewModel {
        val layout = when (viewType) {
            VIEW_TYPE_NORMAL -> R.layout.item_note
            VIEW_TYPE_SELECTED -> R.layout.item_note_selected
            VIEW_TYPE_WITH_IMAGE_NORMAL -> R.layout.item_note_with_image
            VIEW_TYPE_WITH_IMAGE_SELECTED -> R.layout.item_note_with_image_selected

            else -> throw java.lang.RuntimeException("Unknown view type: $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return NoteItemViewModel(view)
    }


    override fun onBindViewHolder(holder: NoteItemViewModel, position: Int) {
        val noteItem = getItem(position)

        if (holder.itemViewType == VIEW_TYPE_WITH_IMAGE_NORMAL || holder.itemViewType == VIEW_TYPE_WITH_IMAGE_SELECTED){
            holder.image?.setImageBitmap(noteItem.image)
        }
        holder.tvTitle.text = noteItem.title
        holder.tvBody.text = noteItem.body
        holder.itemView.setOnLongClickListener {
            onNoteItemLongClickListener?.invoke(noteItem)
            true
        }
        holder.itemView.setOnClickListener {
            onNoteItemClickListener?.invoke(noteItem)
        }


    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).image == null) {
            VIEW_TYPE_NORMAL
        } else {
            VIEW_TYPE_WITH_IMAGE_NORMAL
        }
    }


    companion object {
        const val VIEW_TYPE_NORMAL = 0
        const val VIEW_TYPE_SELECTED = 1
        const val VIEW_TYPE_WITH_IMAGE_NORMAL = 2
        const val VIEW_TYPE_WITH_IMAGE_SELECTED = 3

        const val MAX_POOL_SIZE = 15
    }
}
