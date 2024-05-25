package com.mirea.kt.ribo.notes.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mirea.kt.ribo.notes.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val vm by viewModel<MainViewModel>()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide();

        binding.signInButton.setOnClickListener {
            Log.d(
                "DEBUG_TASK", "BUTTON CLICKED"
            )

            CoroutineScope(Dispatchers.IO).launch {

                val task = vm.getStudentTask(
                    "Student52644",
                    "wN5B8yN",
                    "RIBO-03-22"
                )

                runOnUiThread {
                    Log.d(
                        "DEBUG_TASK", task.task
                    )
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }

    }
}