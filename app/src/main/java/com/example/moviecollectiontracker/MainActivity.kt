package com.example.moviecollectiontracker

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), MovieAdapter.OnItemClickListener {
    private lateinit var dbHelper: MovieDatabaseHelper
    private lateinit var movieList: ArrayList<Movie>
    private lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = MovieDatabaseHelper(this)

        val rvMovies = findViewById<RecyclerView>(R.id.rvMovies)
        val btnAddMovie = findViewById<Button>(R.id.btnAddMovie)

        // RecyclerView setup
        movieList = dbHelper.getAllMovies()
        adapter = MovieAdapter(movieList, this)
        rvMovies.adapter = adapter
        rvMovies.layoutManager = LinearLayoutManager(this)

        btnAddMovie.setOnClickListener {
            val etTitle = findViewById<EditText>(R.id.etTitle)
            val etGenre = findViewById<EditText>(R.id.etGenre)
            val etReleaseDate = findViewById<EditText>(R.id.etReleaseDate)

            val title = etTitle.text.toString()
            val genre = etGenre.text.toString()
            val releaseDate = etReleaseDate.text.toString()

            if (title.isNotEmpty() && genre.isNotEmpty() && releaseDate.isNotEmpty()) {
                val newMovie = Movie(0, title, genre, releaseDate)
                dbHelper.insertMovie(newMovie)
                movieList.add(newMovie)
                adapter.notifyItemInserted(movieList.size - 1)

                etTitle.text.clear()
                etGenre.text.clear()
                etReleaseDate.text.clear()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onEditClick(position: Int) {
        val movie = movieList[position]

        // Inflate the dialog layout
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_movie, null)
        val etEditTitle = dialogView.findViewById<EditText>(R.id.etEditTitle)
        val etEditGenre = dialogView.findViewById<EditText>(R.id.etEditGenre)
        val etEditReleaseDate = dialogView.findViewById<EditText>(R.id.etEditReleaseDate)

        // Pre-fill the fields with the current movie data
        etEditTitle.setText(movie.title)
        etEditGenre.setText(movie.genre)
        etEditReleaseDate.setText(movie.releaseDate)

        // Create and show the dialog
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Edit Movie")
            .setPositiveButton("Update", null)
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        val dialog = dialogBuilder.create()

        // Handle the "Update" button click after the dialog is shown
        dialog.setOnShowListener {
            val btnUpdate = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            btnUpdate.setOnClickListener {
                val updatedTitle = etEditTitle.text.toString()
                val updatedGenre = etEditGenre.text.toString()
                val updatedReleaseDate = etEditReleaseDate.text.toString()

                if (updatedTitle.isNotEmpty() && updatedGenre.isNotEmpty() && updatedReleaseDate.isNotEmpty()) {
                    // Update the movie in the database
                    dbHelper.updateMovie(movie.id, updatedTitle, updatedGenre, updatedReleaseDate)

                    // Update the movie in the list and notify the adapter
                    movieList[position] = Movie(movie.id, updatedTitle, updatedGenre, updatedReleaseDate)
                    adapter.notifyItemChanged(position)

                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            }
        }
        dialog.show()
    }

    override fun onDeleteClick(position: Int) {
        val movie = movieList[position]
        dbHelper.deleteMovie(movie.id)
        movieList.removeAt(position)
        adapter.notifyItemRemoved(position)
    }
}
