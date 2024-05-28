package com.mirea.kt.ribo.notes.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.mirea.kt.ribo.notes.databinding.ActivityNoteItemBinding
import com.mirea.kt.ribo.notes.domain.note.NoteItem
import org.koin.androidx.viewmodel.ext.android.viewModel

class NoteItemActivity : AppCompatActivity() {
    private var screenMode = MODE_UNKNOWN
    private var noteItemId = NoteItem.UNDEFINED_INT

    private val vm by viewModel<MainViewModel>()
    private lateinit var binding: ActivityNoteItemBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DEBUG_GG", "ACTIVITY CREATED")
        binding = ActivityNoteItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        parseIntent()
        parseActivity()
        setEditTextListeners()

        setEditText()

        binding.shareButton.setOnClickListener {
            shareContent()
        }

        binding.doneButton.setOnClickListener {
            saveChanged()
        }

    }

    private fun saveChanged() {
        if (screenMode == MODE_ADD) {
            vm.addNote(
                NoteItem(
                    title = binding.titleEditText.text.toString(),
                    body = binding.bodyEditText.text.toString()
                )
            )
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        } else if ((screenMode == MODE_EDIT)) {
            vm.editNote(
                NoteItem(
                    id = noteItemId,
                    title = binding.titleEditText.text.toString(),
                    body = binding.bodyEditText.text.toString()
                )
            )
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        }
        finish()
    }

    private fun shareContent() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TITLE, binding.titleEditText.text)
            putExtra(Intent.EXTRA_TEXT, binding.bodyEditText.text)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun setEditText() {
        val titleET = binding.titleEditText
        titleET.requestFocus()
        titleET.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        titleET.setRawInputType(InputType.TYPE_CLASS_TEXT);
        WindowCompat.getInsetsController(window, binding.titleEditText)
            .show(WindowInsetsCompat.Type.ime())
    }


    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            noteItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, NoteItem.UNDEFINED_INT)

        }

    }

    private fun parseActivity() {

        if (screenMode != MODE_EDIT && screenMode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode $screenMode")
        }
        if (screenMode == MODE_EDIT) {
            val noteItem = vm.getNote(noteItemId)
//            if (noteItem.image != null){
//                binding.image.src = ......
//            } TODO("IMPLEMENT THIS")
            binding.titleEditText.setText(noteItem.title)
            binding.bodyEditText.setText(noteItem.body)
        }
    }

    private fun setEditTextListeners() {
        with(binding) {
            titleEditText.addTextChangedListener {
                if (binding.titleEditText.text.toString() != "" || binding.bodyEditText.text.toString() != "") {
                    doneButton.visibility = View.VISIBLE
                    shareButton.visibility = View.VISIBLE

                } else {
                    doneButton.visibility = View.GONE
                    shareButton.visibility = View.GONE
                }
            }


            bodyEditText.addTextChangedListener {
                if (binding.titleEditText.text.toString() != "" || binding.bodyEditText.text.toString() != "") {
                    doneButton.visibility = View.VISIBLE
                    shareButton.visibility = View.VISIBLE

                } else {
                    doneButton.visibility = View.GONE
                    shareButton.visibility = View.GONE
                }
            }
        }

    }


    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""


        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, NoteItemActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, noteItemId: Int): Intent {
            val intent = Intent(context, NoteItemActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, noteItemId)
            return intent
        }
    }
}
