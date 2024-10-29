package com.example.moviecollectiontracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MovieAdapter(private val movies: ArrayList<Movie>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val genre: TextView = itemView.findViewById(R.id.tvGenre)
        val releaseDate: TextView = itemView.findViewById(R.id.tvReleaseDate)
        private val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
        private val btnDelete: Button = itemView.findViewById(R.id.btnDelete)

        init {
            btnEdit.setOnClickListener { listener.onEditClick(adapterPosition) }
            btnDelete.setOnClickListener { listener.onDeleteClick(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.title.text = movie.title
        holder.genre.text = movie.genre
        holder.releaseDate.text = movie.releaseDate
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    interface OnItemClickListener {
        fun onEditClick(position: Int)
        fun onDeleteClick(position: Int)
    }
}
