package com.mirea.kt.ribo.notes.domain

data class Task(
    val data: List<Any>,
    val result_code: Int,
    val task: String,
    val title: String,
    val variant: Int
)