package com.raq.violentclock.`interface`

import com.raq.violentclock.data.Devices
import com.raq.violentclock.data.SpotifyPostSong
import com.raq.violentclock.data.Tracks
import retrofit2.Call
import retrofit2.http.*

private const val TOKEN = "Bearer BQBI77wZKM_hxQVESzDdojKrRSjuUAF3j-1aLVH26_EetHZ06qFMboJ3GLUkSGqzblTPQXFgPkwg67uRauBnCkBM4PgwABSMQZUUmn-4gMhA8R_yfpi8j7fCXHYSTlV9bJBUH3IbyJhMdsNAOjNqPsFv9jMsy40Sqwo"
private const val SEARCH_TYPE = "track"
private const val RESULTS_LIMIT = 20

interface SpotifyInterface {
    @GET("/v1/search")
    fun findSongByName(
        @Header("Authorization")
        header: String = TOKEN,
        @Query("q")
        search: String,
        @Query("type")
        type: String = SEARCH_TYPE,
        @Query("limit")
        limit: Int = RESULTS_LIMIT
    ): Call<Tracks>

    @GET("/v1/me/player/devices")
    fun getDevices(
        @Header("Authorization")
        header: String = TOKEN
    ): Call<Devices>

    @PUT("/v1/me/player/play")
    fun playSong(
        @Header("Authorization")
        header: String = TOKEN,
        @Query("device_id")
        device_id: String,
        @Body
        body: SpotifyPostSong = SpotifyPostSong(context_uri = "spotify:artist:7i3eGEz3HNFnPOCdc7mqoq")
    ): Call<String>

}