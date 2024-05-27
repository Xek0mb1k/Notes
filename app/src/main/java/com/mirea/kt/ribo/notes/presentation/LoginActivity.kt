package com.mirea.kt.ribo.notes.presentation

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
        supportActionBar!!.hide()


        binding.secretButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }

        binding.signInButton.setOnClickListener {

            if (checkAllFields()) {
                binding.progressBar.visibility = View.VISIBLE
                CoroutineScope(Dispatchers.IO).launch {
                    val task = vm.getStudentTask(
                        binding.loginEditText.text.toString(),
                        binding.passwordEditText.text.toString(),
                        STUDENT_GROUP
                    )

                    runOnUiThread {
                        Log.d(
                            "DEBUG_TASK", task.toString()
                        )
                        Log.d(
                            "DEBUG_TASK", binding.loginEditText.text.toString() + "\n" +
                                    binding.passwordEditText.text.toString() + "\n" +
                                    STUDENT_GROUP
                        )
                        if (task.result_code > 0) {
                            binding.errorMessageTextView.visibility = View.INVISIBLE

                            Toast.makeText(this@LoginActivity, task.task, Toast.LENGTH_LONG).show()

                            binding.progressBar.visibility = View.VISIBLE


                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("TITLE", task.title)
                            intent.putExtra("BODY", task.task)
                            startActivity(intent)
                            binding.progressBar.visibility = View.INVISIBLE

                        } else {
                            binding.progressBar.visibility = View.INVISIBLE
                            binding.errorMessageTextView.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        val animDrawable = binding.main.background as AnimationDrawable
        animDrawable.setEnterFadeDuration(2500)
        animDrawable.setExitFadeDuration(5000)
        animDrawable.start()
    }



    private fun checkAllFields(): Boolean {
        var result = true

        if (binding.loginEditText.text.isBlank()) {
            binding.loginEditTextInputLayout.isErrorEnabled = true
            binding.loginEditTextInputLayout.error = "Login is required"
            result = false
        } else {
            binding.loginEditTextInputLayout.isErrorEnabled = false
        }
        if (binding.passwordEditText.text.isBlank()) {
            binding.passwordEditTextInputLayout.isErrorEnabled = true
            binding.passwordEditTextInputLayout.error = "Password is required"
            result = false
        } else {
            binding.passwordEditTextInputLayout.isErrorEnabled = false
        }
        return result
    }

    companion object {
        const val STUDENT_GROUP = "RIBO-03-22"
    }
}