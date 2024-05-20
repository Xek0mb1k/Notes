package com.mirea.kt.ribo.notes.di

import com.mirea.kt.ribo.notes.data.NotesRepositoryImpl
import com.mirea.kt.ribo.notes.domain.NotesRepository
import com.mirea.kt.ribo.notes.presentation.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<NotesRepository> {
        NotesRepositoryImpl(androidContext())
    }

    viewModel { MainViewModel(get()) }
}
