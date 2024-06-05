package com.mirea.kt.ribo.notes.presentation


import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.scale
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.mirea.kt.ribo.notes.databinding.ActivityNoteItemBinding
import com.mirea.kt.ribo.notes.domain.note.NoteItem
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream


class NoteItemActivity : AppCompatActivity() {
    private var screenMode = MODE_UNKNOWN
    private var noteItemId = NoteItem.UNDEFINED_INT
    private var image: Bitmap? = null


    private val pickMedia = registerForActivityResult(PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            binding.doneButton.visibility = View.VISIBLE
            @Suppress("DEPRECATION") val bitmap =
                compressBitmap(MediaStore.Images.Media.getBitmap(this.contentResolver, uri))


            image = bitmap

            binding.imageView.setImageBitmap(image)
            binding.imageView.visibility = View.VISIBLE
//            Glide.with(applicationContext).load(image).into(binding.imageView)

        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }


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

        binding.backToHomeButton.setOnClickListener {
            if (checkAllFieldsIsNotEmpty()) {
                saveChanged()
            } else finish()
        }

        binding.shareButton.setOnClickListener {
            shareContent()
        }

        binding.doneButton.setOnClickListener {
            saveChanged()
        }

        binding.imageButton.setOnClickListener {
            setImage()
        }

        binding.imageView.setOnClickListener {
            deleteImage()
        }

    }

    private fun deleteImage() {
        val title = "Delete image?"
        val body = intent.getStringExtra("BODY")
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(body)
            .setPositiveButton("Delete") { _, _ ->
                image = null
                binding.doneButton.visibility = View.VISIBLE
                binding.imageView.setImageBitmap(null)
                binding.imageView.visibility = View.GONE
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .show()
    }

    private fun setImage() {

        pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))

    }


    private fun saveChanged() {
        val title =
            binding.titleEditText.text.toString()
                .trim()
        val body = binding.bodyEditText.text.toString()
            .trim()

        if (screenMode == MODE_ADD && checkAllFieldsIsNotEmpty()) {
            vm.addNote(
                NoteItem(
                    title = title,
                    body = body,
                    image = image
                )
            )
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
        } else if (screenMode == MODE_EDIT) {
            if (title.replace(" ", "") == "" && body.replace(" ", "") == "" && image == null) {
                vm.deleteNote(noteItemId)
                Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show()
            } else {
                vm.editNote(
                    NoteItem(
                        id = noteItemId,
                        title = title,
                        body = body,
                        image = image
                    )
                )
                Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
            }
        }

        finish()
    }

    private fun shareContent() {
        val clipboardIntent = Intent()
        clipboardIntent.putExtra(
            Intent.EXTRA_TEXT,
            "" + binding.titleEditText.text + "\n" + binding.bodyEditText.text
        )
        clipboardIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject")

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND

            putExtra(
                Intent.EXTRA_TEXT,
                "" + binding.titleEditText.text + "\n" + binding.bodyEditText.text
            )

            type = "text/plain"
        }


        val shareIntent = Intent.createChooser(sendIntent, "Share via")
        shareIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, clipboardIntent)
        startActivity(shareIntent)
    }

    private fun setEditText() {
        val titleET = binding.titleEditText
        titleET.requestFocus()
        titleET.setImeOptions(EditorInfo.IME_ACTION_NEXT)
        titleET.setRawInputType(InputType.TYPE_CLASS_TEXT)
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
            if (noteItem.image != null) {
                binding.imageView.setImageBitmap(noteItem.image)
                binding.imageView.visibility = View.VISIBLE
                image = noteItem.image
            }
            binding.shareButton.visibility = View.VISIBLE
            binding.titleEditText.setText(noteItem.title)
            binding.bodyEditText.setText(noteItem.body)
        }
    }

    private fun setEditTextListeners() {
        with(binding) {
            titleEditText.addTextChangedListener {
                if (checkAllFieldsIsNotEmpty()) {
                    doneButton.visibility = View.VISIBLE
                    shareButton.visibility = View.VISIBLE

                } else {
                    shareButton.visibility = View.GONE
                }
            }


            bodyEditText.addTextChangedListener {
                if (checkAllFieldsIsNotEmpty()) {
                    doneButton.visibility = View.VISIBLE
                    shareButton.visibility = View.VISIBLE

                } else {
                    shareButton.visibility = View.GONE
                }
            }
        }

    }

    private fun checkAllFieldsIsNotEmpty() =
        binding.titleEditText.text.toString()
            .trimIndent()
            .replace("\\s+".toRegex(), " ").replace(" ", "") != "" ||
                binding.bodyEditText.text.toString()
                    .trimIndent()
                    .replace("\\s+".toRegex(), " ").replace(" ", "") != "" ||
                image != null


    private fun compressBitmap(bitmap: Bitmap, quality: Int = DEFAULT_QUALITY): Bitmap {
        Log.d("IMAGE_COMPRESSOR", "=========================================")
        Log.d("IMAGE_COMPRESSOR", "BEFORE ${bitmap.byteCount / 8 / 1024} KB")

        val ratio = bitmap.width.toDouble() / bitmap.height
        Log.d("IMAGE_COMPRESSOR", "BE COMPRESS ${bitmap.width} ${bitmap.height} $ratio ")
        val bit = bitmap.scale((HEIGHT * ratio).toInt(), HEIGHT, true)

        val stream = ByteArrayOutputStream()
        bit.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        val byteArray = stream.toByteArray()
        Log.d("IMAGE_COMPRESSOR", "AFTER ${bit.byteCount / 8 / 1024} KB")

        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

    }

    companion object {
        const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        const val HEIGHT = 800
        const val DEFAULT_QUALITY = 80


        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, NoteItemActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, noteItemId: Int): Intent {
            val intent = Intent(context, NoteItemActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, noteItemId)
            return intent
        }
    }
}
