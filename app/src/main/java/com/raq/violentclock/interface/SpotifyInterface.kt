package com.raq.violentclock.`interface`

import com.raq.violentclock.data.Devices
import com.raq.violentclock.data.SpotifyPostSong
import com.raq.violentclock.data.Tracks
import retrofit2.Call
import retrofit2.http.*

private const val TOKEN = "Bearer BQA83-O7UHgGAI7ADkG9FEMfOnetX6wIyu-xPylfOIjklpDhrRUeXMYW8V5EyYwfqPJJ5LcddMhJe5ZoSj10l6LF5ZBuCtcYEAT8NDI1Y1iBruL5M3gjh8t7XvhLo9x2OgqRwSSThqCg9VaPhMgvRv442efcHsvOZT0"
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