package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.dataClass.Album
import org.json.JSONException



class TopAlbumsFragment : Fragment() {

    private lateinit var albumRecyclerView: RecyclerView
    private lateinit var albumAdapter: AlbumAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_top_albums, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        albumRecyclerView = requireView().findViewById(R.id.album_recycler_view)
        albumRecyclerView.layoutManager = LinearLayoutManager(context)

        albumAdapter = AlbumAdapter(ArrayList())
        albumRecyclerView.adapter = albumAdapter

        // Get albums for the selected genre using Last.fm API
        val queue = Volley.newRequestQueue(requireContext())
        val apiKey = "ce45be56de750949fc5c89408b94dc24"
        val genre = requireActivity().intent.getStringExtra("selected_genre") ?: ""
        val url =
            "https://ws.audioscrobbler.com/2.0/?method=tag.gettopalbums&tag=$genre&api_key=ce45be56de750949fc5c89408b94dc24&format=json"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                // Parse the response JSON and add each album to the adapter
                for (i in 0 until response.length()) {
                    try {
                        val albumObject = response.getJSONObject(i)
                        val album = Album(
                            albumObject.getString("name"),
                            albumObject.getJSONObject("artist").getString("name"),
                            albumObject.getJSONArray("image").getJSONObject(2).getString("#text")
                        )
                        albumAdapter.addAlbum(album)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            },
            { error ->
                // Handle error
                error.printStackTrace()
            })

        queue.add(jsonArrayRequest)
    }

    private inner class AlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val albumCoverImageView: ImageView =
            itemView.findViewById(R.id.album_cover_image_view)
        private val albumTitleTextView: TextView = itemView.findViewById(R.id.album_title_text_view)
        private val albumArtistTextView: TextView =
            itemView.findViewById(R.id.album_artist_text_view)

        fun bind(album: Album) {
            albumTitleTextView.text = album.title
            albumArtistTextView.text = album.artist
            Glide.with(this.itemView.context)
                .load(album.coverImageUrl)
                .placeholder(R.drawable.album_placeholder) // default image
                .into(albumCoverImageView)
        }
    }

    private inner class AlbumAdapter(private val albumList: MutableList<Album>) :
        RecyclerView.Adapter<AlbumViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.album_list_item, parent, false)
            return AlbumViewHolder(view)
        }

        override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
            val album = albumList[position]
            holder.bind(album)
        }

        override fun getItemCount(): Int {
            return albumList.size
        }

        fun addAlbum(album: Album) {
            albumList.add(album)
            notifyItemInserted(albumList.size - 1)
        }
    }
}

