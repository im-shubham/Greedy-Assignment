package com.example.myapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.myapplication.R
import com.example.myapplication.dataClass.Genre

class GenreAdapter(
    private val context: Context,
    private var genres: MutableList<Genre>,
    private val onGenreClickListener: (Genre) -> Unit
) : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.genre_items, parent, false)
        return GenreViewHolder(view)
    }

    override fun getItemCount(): Int {
        return genres.size
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genre = genres[position]
        holder.bind(genre)
        holder.itemView.setOnClickListener {
            onGenreClickListener(genre)
        }
    }

    inner class GenreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val genreName: TextView = itemView.findViewById(R.id.genre_name)

        fun bind(genre: Genre) {
            genreName.text = genre.name
        }
    }

    fun updateGenres(genres: List<Genre>) {
        this.genres.clear()
        this.genres.addAll(genres)
        notifyDataSetChanged()
    }
}
