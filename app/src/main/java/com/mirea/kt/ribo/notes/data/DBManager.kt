package com.mirea.kt.ribo.notes.data


import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import com.mirea.kt.ribo.notes.data.NoteDatabaseHelper.Companion.COLUMN_BODY
import com.mirea.kt.ribo.notes.data.NoteDatabaseHelper.Companion.COLUMN_COLOR
import com.mirea.kt.ribo.notes.data.NoteDatabaseHelper.Companion.COLUMN_IMAGE
import com.mirea.kt.ribo.notes.data.NoteDatabaseHelper.Companion.COLUMN_NOTEBOOK_ID
import com.mirea.kt.ribo.notes.data.NoteDatabaseHelper.Companion.COLUMN_NOTE_ID
import com.mirea.kt.ribo.notes.data.NoteDatabaseHelper.Companion.COLUMN_REMIND_TIME
import com.mirea.kt.ribo.notes.data.NoteDatabaseHelper.Companion.COLUMN_TITLE
import com.mirea.kt.ribo.notes.data.NoteDatabaseHelper.Companion.TABLE_NOTEBOOKS
import com.mirea.kt.ribo.notes.data.NoteDatabaseHelper.Companion.TABLE_NOTES
import com.mirea.kt.ribo.notes.domain.note.NoteItem
import com.mirea.kt.ribo.notes.domain.notebook.NotebookItem
import java.io.ByteArrayOutputStream


class DBManager(private val sqLiteHelper: SQLiteOpenHelper) {
    fun addNote(noteItem: NoteItem) {
        val db = sqLiteHelper.writableDatabase

        val cv = ContentValues()
        val imageArray = noteItem.image?.let { getBitmapAsByteArray(it) }

        cv.put(COLUMN_TITLE, noteItem.title)
        cv.put(COLUMN_BODY, noteItem.body)
        cv.put(COLUMN_IMAGE, imageArray)
        cv.put(COLUMN_REMIND_TIME, noteItem.remindTime)
        cv.put(COLUMN_COLOR, noteItem.color)
        cv.put(COLUMN_NOTEBOOK_ID, noteItem.notebookId)

        db.insert(TABLE_NOTES, null, cv)
        cv.clear()
        db.close()
    }

    fun deleteNote(id: Int) {
        val db = sqLiteHelper.writableDatabase
        db.delete(
            TABLE_NOTES,
            "$COLUMN_NOTE_ID = ?", arrayOf(id.toString())
        )
        db.close()
    }

    fun editNote(noteItem: NoteItem) {
        val cv = ContentValues()
        val imageArray = noteItem.image?.let { getBitmapAsByteArray(it) }

        cv.put(COLUMN_TITLE, noteItem.title)
        cv.put(COLUMN_BODY, noteItem.body)
        cv.put(COLUMN_IMAGE, imageArray)
        cv.put(COLUMN_REMIND_TIME, noteItem.remindTime)
        cv.put(COLUMN_COLOR, noteItem.color)
        cv.put(COLUMN_NOTEBOOK_ID, noteItem.notebookId)

        val db = sqLiteHelper.writableDatabase
        db.update(
            TABLE_NOTES,
            cv,
            "$COLUMN_NOTE_ID = ?",
            arrayOf(noteItem.id.toString())
        )
        db.close()
    }

    fun getNote(id: Int): NoteItem {
        val db = sqLiteHelper.readableDatabase
        var noteItem = NoteItem()

        val selection = "$COLUMN_NOTE_ID = ?"
        val selectionArgs = arrayOf(id.toString())

        val cursor = db.query(
            TABLE_NOTES,
            arrayOf(
                COLUMN_TITLE,
                COLUMN_BODY,
                COLUMN_IMAGE,
                COLUMN_REMIND_TIME,
                COLUMN_COLOR,
                COLUMN_NOTEBOOK_ID
            ),
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            val title = cursor.getString(
                cursor.getColumnIndexOrThrow(COLUMN_TITLE)
            )
            val body = cursor.getString(
                cursor.getColumnIndexOrThrow(COLUMN_BODY)
            )
            val imageByteArray = cursor.getBlob(
                cursor.getColumnIndexOrThrow(COLUMN_IMAGE)
            )
            val image = byteToBitmap(imageByteArray)

            val remindTime = cursor.getInt(
                cursor.getColumnIndexOrThrow(COLUMN_REMIND_TIME)
            )
            val color = cursor.getInt(
                cursor.getColumnIndexOrThrow(COLUMN_COLOR)
            )
            val notebookId = cursor.getInt(
                cursor.getColumnIndexOrThrow(COLUMN_NOTEBOOK_ID)
            )

            noteItem = NoteItem(
                id,
                title,
                body,
                image,
                remindTime,
                color,
                notebookId
            )
        }

        cursor.close()
        db.close()

        return noteItem
    }


    fun getAllNotesFromDatabase(): ArrayList<NoteItem> {
        val notes: ArrayList<NoteItem> = ArrayList()
        val db = sqLiteHelper.writableDatabase
        val dbCursor = db.query(
            TABLE_NOTES, null,
            null, null, null, null, null
        )

        if (dbCursor.moveToFirst()) {
            do {
                val id = dbCursor.getInt(
                    dbCursor.getColumnIndexOrThrow(COLUMN_NOTE_ID)
                )
                val title = dbCursor.getString(
                    dbCursor.getColumnIndexOrThrow(COLUMN_TITLE)
                )
                val body = dbCursor.getString(
                    dbCursor.getColumnIndexOrThrow(COLUMN_BODY)
                )
                val image = dbCursor.getBlob(
                    dbCursor.getColumnIndexOrThrow(COLUMN_IMAGE)
                )
                val remindTime = dbCursor.getInt(
                    dbCursor.getColumnIndexOrThrow(COLUMN_REMIND_TIME)
                )
                val color = dbCursor.getInt(
                    dbCursor.getColumnIndexOrThrow(COLUMN_COLOR)
                )
                val notebookId = dbCursor.getInt(
                    dbCursor.getColumnIndexOrThrow(COLUMN_NOTEBOOK_ID)
                )

                notes.add(
                    NoteItem(
                        id,
                        title,
                        body,
                        byteToBitmap(image),
                        remindTime,
                        color,
                        notebookId
                    )
                )
            } while (dbCursor.moveToNext())
        }
        dbCursor.close()
        db.close()
        return notes
    }


    fun addNotebook(notebookItem: NotebookItem) {
        val db = sqLiteHelper.writableDatabase

        val cv = ContentValues()

        cv.put(COLUMN_TITLE, notebookItem.title)
        cv.put(COLUMN_COLOR, notebookItem.color)

        db.insert(TABLE_NOTEBOOKS, null, cv)
        cv.clear()
        db.close()
    }

    fun deleteNotebook(notebookId: Int) {
        val db = sqLiteHelper.writableDatabase
        db.delete(
            TABLE_NOTEBOOKS,
            "$COLUMN_NOTEBOOK_ID = ?", arrayOf(notebookId.toString())
        )
        db.close()
    }

    fun editNotebook(notebookItem: NotebookItem) {
        val cv = ContentValues()

        cv.put(COLUMN_TITLE, notebookItem.title)
        cv.put(COLUMN_COLOR, notebookItem.color)

        val db = sqLiteHelper.writableDatabase
        db.update(
            TABLE_NOTEBOOKS,
            cv,
            "$COLUMN_NOTE_ID = ?",
            arrayOf(notebookItem.id.toString())
        )
        db.close()
    }

    fun getNotebookList(): List<NotebookItem> {
        val notebooks: ArrayList<NotebookItem> = ArrayList()
        val db = sqLiteHelper.writableDatabase
        val dbCursor = db.query(
            TABLE_NOTEBOOKS, null,
            null, null, null, null, null
        )

        if (dbCursor.moveToFirst()) {
            do {
                val id = dbCursor.getInt(
                    dbCursor.getColumnIndexOrThrow(COLUMN_NOTE_ID)
                )
                val title = dbCursor.getString(
                    dbCursor.getColumnIndexOrThrow(COLUMN_TITLE)
                )
                val color = dbCursor.getInt(
                    dbCursor.getColumnIndexOrThrow(COLUMN_COLOR)
                )

                notebooks.add(
                    NotebookItem(
                        id,
                        title,
                        color
                    )
                )
            } while (dbCursor.moveToNext())
        }
        dbCursor.close()
        db.close()
        return notebooks
    }

    fun getNotesListFromNotebook(notebookId: Int): List<NoteItem> {
        val notesList = mutableListOf<NoteItem>()
        val db = sqLiteHelper.readableDatabase

        val selection = "$COLUMN_NOTEBOOK_ID = ?"
        val selectionArgs = arrayOf(notebookId.toString())

        val cursor = db.query(
            TABLE_NOTES,
            arrayOf(
                COLUMN_NOTE_ID,
                COLUMN_TITLE,
                COLUMN_BODY,
                COLUMN_IMAGE,
                COLUMN_REMIND_TIME,
                COLUMN_COLOR
            ),
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            val noteId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NOTE_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val body = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BODY))
            val imageByteArray = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))
            val image = byteToBitmap(imageByteArray)
            val remindTime = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REMIND_TIME))
            val color = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COLOR))

            val noteItem = NoteItem(noteId, title, body, image, remindTime, color, notebookId)
            notesList.add(noteItem)
        }

        cursor.close()
        db.close()

        return notesList
    }

    private fun getBitmapAsByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(CompressFormat.PNG, 0, outputStream)
        return outputStream.toByteArray()
    }

    private fun byteToBitmap(imgByteArray: ByteArray?): Bitmap? {
        return if (imgByteArray == null) {
            null
        } else {
            BitmapFactory.decodeByteArray(imgByteArray, 0, imgByteArray.size)
        }
    }
}
