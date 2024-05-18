package com.mirea.kt.ribo.notes.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.mirea.kt.ribo.notes.domain.note.NoteItem

class NoteDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "NoteDatabase.db"
        private const val TABLE_NOTES = "notes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_IMAGE = "image"
        private const val COLUMN_REMIND_TIME = "content"
        private const val COLUMN_COLOR = "content"
        private const val COLUMN_NOTEBOOK_ID = "content"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = ("CREATE TABLE $TABLE_NOTES ($COLUMN_ID INTEGER PRIMARY KEY, "
                + "$COLUMN_TITLE TEXT, "
                + "$COLUMN_CONTENT TEXT)"
                )
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NOTES")
        onCreate(db)
    }

    fun addNote(noteItem: NoteItem): Long {
        val values = ContentValues()
        values.put(COLUMN_TITLE, noteItem.title)
        values.put(COLUMN_CONTENT, noteItem.body)

        val db = this.writableDatabase
        val newRowId = db.insert(TABLE_NOTES, null, values)
        db.close()

        return newRowId
    }

    fun deleteNote(id: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_NOTES, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()

        return result
    }

    fun editNote(noteItem: NoteItem): Int {
        val values = ContentValues()
        values.put(COLUMN_TITLE, noteItem.title)
        values.put(COLUMN_CONTENT, noteItem.body)

        val db = this.writableDatabase
        val result =
            db.update(TABLE_NOTES, values, "$COLUMN_ID = ?", arrayOf(noteItem.id.toString()))
        db.close()

        return result
    }

}