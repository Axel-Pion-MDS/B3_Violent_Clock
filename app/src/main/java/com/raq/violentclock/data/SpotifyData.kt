package com.raq.violentclock.data

import kotlin.collections.ArrayList

data class Tracks(
    val tracks: Items
)

data class Items(
    val items: ArrayList<Album>
)

data class Album(
    val album: SpotifyData
)

data class Artists(
    val name: String
)

data class Images(
    val url: String?,
    val height: Int?,
    val width: Int?
)

data class SpotifyData(
    val artists: ArrayList<Artists>,
    val images: ArrayList<Images>?,
    val name: String,
    val href: String,
    val uri: String,
    val id: String
)

