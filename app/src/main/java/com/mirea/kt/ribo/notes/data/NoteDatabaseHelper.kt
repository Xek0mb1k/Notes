package com.mirea.kt.ribo.notes.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.mirea.kt.ribo.notes.domain.note.NoteItem

class NoteDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "NoteDatabase.db"

        const val TABLE_NOTES = "notes"
        const val COLUMN_NOTE_ID = "note_id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_BODY = "body"
        const val COLUMN_IMAGE = "image"
        const val COLUMN_REMIND_TIME = "remind_time"
        const val COLUMN_COLOR = "color"


        const val TABLE_NOTEBOOKS = "notebook"
        const val COLUMN_NOTEBOOK_ID = "notebook_id"

    }

    override fun onCreate(db: SQLiteDatabase) {
        val createNoteTableQuery = (
                "CREATE TABLE $TABLE_NOTES (" +
                        "$COLUMN_NOTE_ID INTEGER PRIMARY KEY autoincrement, " +
                        "$COLUMN_TITLE TEXT, " +
                        "$COLUMN_BODY TEXT, " +
                        "$COLUMN_IMAGE BLOB, " +
                        "$COLUMN_REMIND_TIME INTEGER, " +
                        "$COLUMN_COLOR TEXT, " +
                        "$COLUMN_NOTEBOOK_ID INTEGER, " +
                        "FOREIGN KEY($COLUMN_NOTEBOOK_ID) REFERENCES $TABLE_NOTEBOOKS($COLUMN_NOTE_ID)" +
                        ")"
                )

        val createNotebookTableQuery = (
                "CREATE TABLE $TABLE_NOTEBOOKS (" +
                        "$COLUMN_NOTEBOOK_ID INTEGER PRIMARY KEY autoincrement, " +
                        "$COLUMN_TITLE TEXT, " +
                        "$COLUMN_COLOR TEXT" +
                        ")"
                )
        db.execSQL(createNoteTableQuery)
        db.execSQL(createNotebookTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d("DEBUG_DATABASE", "DATABASE onUpgrade")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NOTES")
        onCreate(db)
    }
}