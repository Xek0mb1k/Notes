package com.mirea.kt.ribo.notes.presentation


import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mirea.kt.ribo.notes.R
import com.mirea.kt.ribo.notes.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

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


    private fun isOnePaneMode(): Boolean {
        return noteItemContainer == null
    }

    private fun launchFragment(fragment: Fragment) {
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
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val searchItem = menu.findItem(R.id.search_view_items)
        val searchView = searchItem.actionView as android.widget.SearchView

        val searchCloseButtonId =
            searchView.findViewById<View>(androidx.appcompat.R.id.search_close_btn).id
        val closeButton = searchView.findViewById<ImageView>(searchCloseButtonId)
        closeButton.setOnClickListener {
            searchView.setQuery("", false)
            searchView.clearFocus()
            Log.d("DEBUG_MENU", "CLOSED SEARCH")

        }
        // Detect SearchView icon clicks
        searchView.setOnSearchClickListener {
            Log.d("DEBUG_MENU", "PRESSED SEARCH")
            setItemsVisibility(
                menu,
                searchItem,
                false
            )
        }

        // Detect SearchView close
        searchView.setOnCloseListener {
            setItemsVisibility(menu, searchItem, true)
            false
        }

        return super.onCreateOptionsMenu(menu)
    }

    private fun setItemsVisibility(menu: Menu, exception: MenuItem, visible: Boolean) {
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            if (item !== exception) item.setVisible(visible)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
//            R.id.search_view_items -> {
//                Log.d("DEBUG_MENU", "SEARCH_BUTTON Pressed")
//                val otherItem: MenuItem = findViewById(R.id.settings)
//                otherItem.setVisible(false)
//                val otherItem2: MenuItem = findViewById(R.id.change_type_of_view)
//                otherItem2.setVisible(false)
//                val otherItem3: MenuItem = findViewById(R.id.change_sorting)
//                otherItem3.setVisible(false)
//
//
//                // Return true if you wish to expand the action view, false otherwise
//                true
//            }

//            R.id.settings -> {
//                Log.d("DEBUG_MENU", "Settings pressed")
//                true
//            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.d("DEBUG_MENU", query.toString())
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Log.d("DEBUG_MENU", newText.toString())
        return true
    }
}