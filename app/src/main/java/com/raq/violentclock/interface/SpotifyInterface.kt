package com.raq.violentclock.`interface`

import com.raq.violentclock.data.Devices
import com.raq.violentclock.data.SpotifyPostSong
import com.raq.violentclock.data.Tracks
import retrofit2.Call
import retrofit2.http.*

private const val SEARCH_TYPE = "track"
private const val RESULTS_LIMIT = 20

interface SpotifyInterface {
    @GET("/v1/search")
    fun findSongByName(
        @Header("Authorization")
        header: String,
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
        header: String
    ): Call<Devices>

    @PUT("/v1/me/player/play")
    fun playSong(
        @Header("Authorization")
        header: String,
        @Query("device_id")
        device_id: String,
        @Body()
        body: SpotifyPostSong
    ): Call<String>

    @PUT("/v1/me/player/pause")
    fun stopSong(
        @Header("Authorization")
        header: String,
        @Query("device_id")
        device_id: String
    ): Call<String>
}