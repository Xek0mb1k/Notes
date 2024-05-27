package com.mirea.kt.ribo.notes.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mirea.kt.ribo.notes.R
import com.mirea.kt.ribo.notes.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val vm by viewModel<MainViewModel>()
    private lateinit var noteListAdapter: NoteListAdapter
    private lateinit var binding: ActivityMainBinding
    private var noteItemContainer: FragmentContainerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showDialog()
        setupRecyclerView()

        noteListAdapter.submitList(vm.getNoteList())


        val buttonAddItem = binding.fabAdd
        buttonAddItem.setOnClickListener {
                val intent = NoteItemActivity.newIntentAddItem(this)
                startActivity(intent)
        }

    }

    private fun isOnePaneMode(): Boolean{
        return noteItemContainer == null
    }
    private fun launchFragment(fragment: Fragment){
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.note_item_container, fragment)
            .addToBackStack(null)
            .commit()
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
        super.onResume()
    }

    private fun setupClickListener() {
        noteListAdapter.onNoteItemClickListener = {
            val intent = NoteItemActivity.newIntentEditItem(this, it.id)
            startActivity(intent)
        }
    }

    private fun showDialog(){
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}