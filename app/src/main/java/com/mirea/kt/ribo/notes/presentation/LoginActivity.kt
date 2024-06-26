package com.mirea.kt.ribo.notes.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.drawable.AnimationDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mirea.kt.ribo.notes.R
import com.mirea.kt.ribo.notes.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginActivity : AppCompatActivity() {

    private val vm by viewModel<MainViewModel>()
    private lateinit var binding: ActivityLoginBinding

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


        binding.secretButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }

        binding.signInButton.setOnClickListener {
            if (!checkInternetConnection(this)) {
                binding.errorMessageTextView.text =
                    getText(R.string.error_message_internet_connection)
                binding.errorMessageTextView.visibility = View.VISIBLE
            } else {
                binding.errorMessageTextView.visibility = View.INVISIBLE
                binding.errorMessageTextView.text = getText(R.string.error_message)
                if (checkAllFields()) {
                    sendRequestAndGetResponse()
                }
            }

        }

        val animDrawable = binding.main.background as AnimationDrawable
        animDrawable.setEnterFadeDuration(2000)
        animDrawable.setExitFadeDuration(4000)
        animDrawable.start()
    }

    private fun sendRequestAndGetResponse() {
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

    @Suppress("DEPRECATION")
    private fun checkInternetConnection(context: Context): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        return networkInfo != null && networkInfo.isConnected
    }

    companion object {
        const val STUDENT_GROUP = "RIBO-03-22"
    }
}