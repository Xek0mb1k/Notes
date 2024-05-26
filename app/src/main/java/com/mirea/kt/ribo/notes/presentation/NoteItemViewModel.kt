package com.mirea.kt.ribo.notes.presentation

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.mirea.kt.ribo.notes.R

class NoteItemViewModel(view: View): RecyclerView.ViewHolder(view) {
    val image: ImageView? = view.findViewById(R.id.image)
    val tvTitle: TextView = view.findViewById(R.id.tv_title)
    val tvBody: TextView = view.findViewById(R.id.tv_body)
    init {
        if (tvTitle.text == ""){
            tvTitle.visibility = View.GONE
        }
        if (tvBody.text == ""){
            tvBody.visibility = View.GONE
        }
    }
}