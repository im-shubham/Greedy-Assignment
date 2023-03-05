package com.example.myapplication
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.fragments.TopAlbumsFragment
import com.example.myapplication.fragments.TopArtistsFragment
import com.example.myapplication.fragments.TopTracksFragment
import com.google.android.material.tabs.TabLayout

import com.google.android.material.tabs.TabLayoutMediator
import org.json.JSONObject

class GenreDetailActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genre_detail)

        val genreTitle = intent.getStringExtra("selected_genre")
        val genreTitleTextView = findViewById<TextView>(R.id.genre_title)
        genreTitleTextView.text = genreTitle

        // Request the description of the selected genre from Last.fm API using Volley
        val queue = Volley.newRequestQueue(this)
        val apiKey = "007b410c39321a948f94fe2cab6e540d"
        val genre = genreTitle // replace with the selected genre
        val url = "https://ws.audioscrobbler.com/2.0/?method=tag.getinfo&tag=$genreTitle&api_key=$apiKey&format=json"

//        val url = "http://ws.audioscrobbler.com/2.0/?method=tag.getinfo&tag=$genreTitle&api_key="8f640e70d53310adca19156326d63ca3"&format=json"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                // Parse the response JSON and extract the description
                val descriptionTextView: TextView = findViewById(R.id.genre_description)
                val jsonObject = JSONObject(response)
                val tagObject = jsonObject.getJSONObject("tag")
                val description = tagObject.getJSONObject("wiki").getString("summary")
                descriptionTextView.text = description
            },
            { error ->
                // Handle error
                Toast.makeText(this, "Error fetching genre description: ${error.message}", Toast.LENGTH_SHORT).show()
            })

        queue.add(stringRequest)

        // TabLayout for top albums, top tracks, top artists
        tabLayout = findViewById(R.id.tab_layout)
        tabLayout.addTab(tabLayout.newTab().setText("Top Albums"))
        tabLayout.addTab(tabLayout.newTab().setText("Top Tracks"))
        tabLayout.addTab(tabLayout.newTab().setText("Top Artists"))

        // ViewPager for the three tabs
        viewPager = findViewById(R.id.view_pager)
        val adapter = GenreDetailPagerAdapter(supportFragmentManager, tabLayout.tabCount)
        viewPager.adapter = adapter

        // Add listener for TabLayout events
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        // Add listener for ViewPager page changes
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
    }

    inner class GenreDetailPagerAdapter(fm: FragmentManager, private val numOfTabs: Int) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> TopAlbumsFragment()
                1 -> TopTracksFragment()
                2 -> TopArtistsFragment()
                else -> throw IllegalArgumentException("Invalid tab position")
            }
        }
        override fun getCount(): Int {
            return numOfTabs
        }
    }
}




