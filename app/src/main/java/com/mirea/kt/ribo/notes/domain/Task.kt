package com.mirea.kt.ribo.notes.domain

data class Task(
    val title: String,
    val task: String,
    val data: List<Any>,
    val result_code: Int,
    val variant: Int
)