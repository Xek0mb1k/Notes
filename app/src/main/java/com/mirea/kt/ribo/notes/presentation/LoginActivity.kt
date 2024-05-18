package com.mirea.kt.ribo.notes.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.mirea.kt.ribo.notes.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val vm by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        Log.d(
            "DEBUG", vm.getStudentTask(
                "Student52644",
                "wN5B8yN",
                "RIBO-03-22"
            ).toString()
        )

    }
}