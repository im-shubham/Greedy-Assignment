package com.example.myapplication.dataClass

data class Genre(
    val id: Int,
    val name: String,
    val description: String,
    val topAlbums: List<Album>,
    val topTracks: List<Track>,
    val topArtists: List<Artist>
)

data class Album(
    val title: String,
    val artist: String,
    val coverImageUrl: String
)


data class Track(
    val title: String,
    val artist: String,
    val coverImageUrl: String
)


data class Artist(val name: String, val imageUrl: String, val listeners: String, val url: String)


