package com.mirea.kt.ribo.notes.presentation


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mirea.kt.ribo.notes.AboutActivity
import com.mirea.kt.ribo.notes.R
import com.mirea.kt.ribo.notes.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val vm by viewModel<MainViewModel>()
    private lateinit var noteListAdapter: NoteListAdapter
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val notebookButton = findViewById<ConstraintLayout>(R.id.notebook_cl)
        notebookButton.setOnClickListener {
            Log.d("DEBUG_MENU", "Pressed notebook")
        }

        showDialog()
        setupRecyclerView()
        setupNotesCounter()

        noteListAdapter.submitList(vm.getNoteList())


        val buttonAddItem = binding.fabAdd
        buttonAddItem.setOnClickListener {
            val intent = NoteItemActivity.newIntentAddItem(this)
            startActivity(intent)
        }

    }

    private fun setupNotesCounter() {
        val counter = findViewById<TextView>(R.id.notes_count_text_view)
        val value = vm.getNoteList().count()
        val note = getString(R.string.notes_counter, value)
        if (value == 0) {
            counter.visibility = View.GONE
            binding.createFirstNoteTextView.visibility = View.VISIBLE
        } else {
            counter.visibility = View.VISIBLE
            binding.createFirstNoteTextView.visibility = View.GONE
        }
        counter.text = note
    }

    private fun setupRecyclerView() {
        val rvNoteList = binding.rvNoteList
        with(rvNoteList) {
            noteListAdapter = NoteListAdapter()
            adapter = noteListAdapter
            layoutManager = LinearLayoutManager(context)

            recycledViewPool.setMaxRecycledViews(
                NoteListAdapter.VIEW_TYPE_NORMAL,
                NoteListAdapter.MAX_POOL_SIZE
            )
            rvNoteList.setLayoutManager(
                StaggeredGridLayoutManager(
                    2,
                    StaggeredGridLayoutManager.VERTICAL
                )
            )
            recycledViewPool.setMaxRecycledViews(
                NoteListAdapter.VIEW_TYPE_WITH_IMAGE_NORMAL,
                NoteListAdapter.MAX_POOL_SIZE
            )
            // to do "ADD OTHER VIEWS"
        }
        setupLongClickListener()
        setupClickListener()
        setupSwipeListener(rvNoteList)

    }

    private fun setupSwipeListener(rvNoteList: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                vm.deleteNote(noteListAdapter.currentList[position].id)

                noteListAdapter.submitList(vm.getNoteList())
                setupNotesCounter()

            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(rvNoteList)
    }

    private fun setupLongClickListener() {
        noteListAdapter.onNoteItemLongClickListener = {
            Log.d("DEBUG", "LONG_PRESSED $it")
        }
    }

    override fun onResume() {
        noteListAdapter.submitList(vm.getNoteList())
        setupNotesCounter()
        super.onResume()
    }

    private fun setupClickListener() {
        noteListAdapter.onNoteItemClickListener = {
            val intent = NoteItemActivity.newIntentEditItem(this, it.id)
            startActivity(intent)
        }
    }

    private fun showDialog() {
        if (intent.hasExtra("TITLE") && intent.hasExtra("BODY")) {
            val title = intent.getStringExtra("TITLE")
            val body = intent.getStringExtra("BODY")
            AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(body)
                .setPositiveButton("OK") { _, _ -> }
                .show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("DEBUG_MENU", "WORKING")

        return when (item.itemId) {
            R.id.about -> {
                val intent = Intent(this@MainActivity, AboutActivity::class.java)
                startActivity(intent)
                Log.d("DEBUG_MENU", "About pressed")
                true
            }


            else -> super.onOptionsItemSelected(item)
        }
    }


}