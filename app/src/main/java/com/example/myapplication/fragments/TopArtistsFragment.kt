package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R




import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.myapplication.dataClass.Artist
import org.json.JSONException

class TopArtistsFragment : Fragment() {

    private lateinit var artistRecyclerView: RecyclerView
    private lateinit var artistAdapter: ArtistAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_top_artists, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        artistRecyclerView = requireView().findViewById(R.id.artist_recycler_view)
        artistRecyclerView.layoutManager = LinearLayoutManager(context)

        artistAdapter = ArtistAdapter(ArrayList())
        artistRecyclerView.adapter = artistAdapter

        // Get artists for the selected genre using Last.fm API
        val apiKey = "ce45be56de750949fc5c89408b94dc24"
        val genre = requireActivity().intent.getStringExtra("selected_genre") ?: ""
        val url = "https://ws.audioscrobbler.com/2.0/?method=tag.gettopartists&tag=$genre&api_key=$apiKey&format=json"

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(requireContext())

        // Request a JSON object response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                // Handle the response
                try {
                    val topArtists = response.getJSONObject("topartists").getJSONArray("artist")
                    for (i in 0 until topArtists.length()) {
                        val artist = topArtists.getJSONObject(i)
                        val name = artist.getString("name")
                        val listeners = artist.getString("listeners")
                        val url = artist.getString("url")
                        val imageUrl = artist.getJSONArray("image").getJSONObject(3).getString("#text")
                        // Create an Artist object with the retrieved information
                        val newArtist = Artist(name, imageUrl, listeners, url)
                        // Add the new artist to the adapter
                        artistAdapter.addArtist(newArtist)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                // Handle errors
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }

    private inner class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val artistCoverImageView: ImageView =
            itemView.findViewById(R.id.artist_cover_image_view)
        private val artistNameTextView: TextView = itemView.findViewById(R.id.artist_name_text_view)

        fun bind(artist: Artist) {
            artistNameTextView.text = artist.name
            Glide.with(this.itemView.context)
                .load(artist.imageUrl)
                .placeholder(R.drawable.artist_placeholder) // default image
                .into(artistCoverImageView)
        }
    }

    private inner class ArtistAdapter(private val artistList: MutableList<Artist>) :
        RecyclerView.Adapter<ArtistViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.artist_list_item, parent, false)
            return ArtistViewHolder(view)
        }

        override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
            val artist = artistList[position]
            holder.bind(artist)
        }

        override fun getItemCount(): Int {
            return artistList.size
        }

        fun addArtist(artist: Artist) {
            artistList.add(artist)
            notifyItemInserted(artistList.size - 1)
        }
    }
}
