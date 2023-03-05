package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.adapters.GenreAdapter
import com.example.myapplication.dataClass.Genre


import android.content.Intent


class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName
    private val EXTRA_GENRE = "selected_genre"

    private lateinit var recyclerView: RecyclerView
    private lateinit var expand_button: ImageButton
    private lateinit var genreListAdapter: GenreAdapter
    private var allGenres = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.genresRecyclerView)
        expand_button = findViewById(R.id.expand_button)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        // Fetch top 10 genres using the Last.fm API
        val url =
            "https://ws.audioscrobbler.com/2.0/?method=tag.getTopTags&api_key=8f640e70d53310adca19156326d63ca3&format=json&limit=10"
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val tags = response.getJSONObject("toptags").getJSONArray("tag")
                val genres = mutableListOf<Genre>()
                for (i in 0 until 10) {
                    val tag = tags.getJSONObject(i)
                    val name = tag.getString("name")
                    genres.add(Genre(0, name, "", emptyList(), emptyList(), emptyList()))
                }
                genreListAdapter = GenreAdapter(this, genres, ::onGenreClick)
                recyclerView.adapter = genreListAdapter
            },
            { error ->
                Log.e(TAG, "Error fetching genres: ${error.message}")
            }
        )

        // Add the request to the request queue using Volley
        Volley.newRequestQueue(this).add(request)

        expand_button.setOnClickListener {
            allGenres = !allGenres
            if (allGenres) {
                // Fetch all genres using the Last.fm API
                val url =
                    "https://ws.audioscrobbler.com/2.0/?method=tag.getTopTags&api_key=8f640e70d53310adca19156326d63ca3&format=json"
                val request = JsonObjectRequest(Request.Method.GET, url, null,
                    { response ->
                        val tags = response.getJSONObject("toptags").getJSONArray("tag")
                        val genres = mutableListOf<Genre>()
                        for (i in 0 until tags.length()) {
                            val tag = tags.getJSONObject(i)
                            val name = tag.getString("name")
                            genres.add(Genre(0, name, "", emptyList(), emptyList(), emptyList()))
                        }
                        genreListAdapter.updateGenres(genres)
                    },
                    { error ->
                        Log.e(TAG, "Error fetching genres: ${error.message}")
                    }
                )
                // Add the request to the request queue using Volley
                Volley.newRequestQueue(this).add(request)
                expand_button.setImageResource(R.drawable.baseline_keyboard_arrow_up_24)
            } else {
                // Fetch top 10 genres using the Last.fm API
                val url =
                    "https://ws.audioscrobbler.com/2.0/?method=tag.getTopTags&api_key=8f640e70d53310adca19156326d63ca3&format=json&limit=10"
                val request = JsonObjectRequest(Request.Method.GET, url, null,
                    { response ->
                        val tags = response.getJSONObject("toptags").getJSONArray("tag")
                        val genres = mutableListOf<Genre>()
                        for (i in 0 until 10) {
                            val tag = tags.getJSONObject(i)
                            val name = tag.getString("name")
                            genres.add(Genre(0, name, "", emptyList(), emptyList(), emptyList()))
                        }
                        genreListAdapter.updateGenres(genres)
                    },
                    { error ->
                        Log.e(TAG, "Error fetching genres: ${error.message}")
                    }
                )
                // Add the request to the request queue using Volley
                Volley.newRequestQueue(this).add(request)
                expand_button.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
            }
        }
    }

    private fun onGenreClick(genre: Genre) {
        val intent = Intent(this, GenreDetailActivity::class.java)
        intent.putExtra(EXTRA_GENRE, genre.name)
        startActivity(intent)
    }

}





