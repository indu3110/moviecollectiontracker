package com.example.moviecollectiontracker

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MovieDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "movies.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_MOVIES = "movies"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_GENRE = "genre"
        private const val COLUMN_RELEASE_DATE = "release_date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_MOVIES (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_TITLE TEXT, " +
                "$COLUMN_GENRE TEXT, " +
                "$COLUMN_RELEASE_DATE TEXT)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_MOVIES")
        onCreate(db)
    }

    fun insertMovie(movie: Movie) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, movie.title)
            put(COLUMN_GENRE, movie.genre)
            put(COLUMN_RELEASE_DATE, movie.releaseDate)
        }
        db.insert(TABLE_MOVIES, null, values)
        db.close()
    }

    fun getAllMovies(): ArrayList<Movie> {
        val movieList = ArrayList<Movie>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_MOVIES"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                val genre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENRE))
                val releaseDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RELEASE_DATE))
                val movie = Movie(id, title, genre, releaseDate)
                movieList.add(movie)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return movieList
    }

    fun updateMovie(id: Int, title: String, genre: String, releaseDate: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_GENRE, genre)
            put(COLUMN_RELEASE_DATE, releaseDate)
        }
        db.update(TABLE_MOVIES, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    fun deleteMovie(id: Int) {
        val db = writableDatabase
        db.delete(TABLE_MOVIES, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }
}
