package com.example.myapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.dataClass.Track
import org.json.JSONException

class TopTracksFragment : Fragment() {

    private lateinit var trackRecyclerView: RecyclerView
    private lateinit var trackAdapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_top_tracks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trackRecyclerView = view.findViewById(R.id.track_recycler_view)
        trackRecyclerView.layoutManager = LinearLayoutManager(context)

        trackAdapter = TrackAdapter(ArrayList())
        trackRecyclerView.adapter = trackAdapter

        // Get tracks for the selected genre using Last.fm API
        val queue = Volley.newRequestQueue(requireContext())
        val apiKey = "2a2e40c481e1367b0adfcbd7cf624777"
        val genre = requireActivity().intent.getStringExtra("selected_genre") ?: ""
        val url =
            "https://ws.audioscrobbler.com/2.0/?method=tag.gettoptracks&tag=$genre&api_key=$apiKey&format=json"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                // Parse the response JSON and add each track to the adapter
                for (i in 0 until response.length()) {
                    try {
                        val trackObject = response.getJSONObject(i)
                        val track = Track(
                            trackObject.getString("name"),
                            trackObject.getJSONObject("artist").getString("name"),
                            trackObject.getJSONArray("image").getJSONObject(2).getString("#text")
                        )
                        trackAdapter.addTrack(track)
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

    private inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val trackCoverImageView: ImageView =
            itemView.findViewById(R.id.track_cover_image_view)
        private val trackTitleTextView: TextView = itemView.findViewById(R.id.track_title_text_view)
        private val trackArtistTextView: TextView =
            itemView.findViewById(R.id.track_artist_text_view)

        fun bind(track: Track) {
            trackTitleTextView.text = track.title
            trackArtistTextView.text = track.artist
            Glide.with(this.itemView.context)
                .load(track.coverImageUrl)
                .placeholder(R.drawable.track_placeholder) // default image
                .into(trackCoverImageView)
        }
    }

    private inner class TrackAdapter(private val trackList: ArrayList<Track>) :
        RecyclerView.Adapter<TrackViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.track_list_item, parent, false)
            return TrackViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
            holder.bind(trackList[position])
        }

        override fun getItemCount(): Int {
            return trackList.size
        }

        fun addTrack(track: Track) {
            trackList.add(track)
            notifyItemInserted(trackList.size - 1)
        }
    }
}

