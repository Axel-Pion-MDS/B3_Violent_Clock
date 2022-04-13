package com.raq.violentclock.`interface`

import com.raq.violentclock.data.Devices
import com.raq.violentclock.data.SpotifyPostSong
import com.raq.violentclock.data.Tracks
import retrofit2.Call
import retrofit2.http.*

private const val TOKEN = "Bearer BQC4_b77tWDlNAkwuRYiLmFpuCnync3z2y-2pMum_0e8Hv4vdOj6uKcpK9loaU1tQDczYORRQUQqm70TGX7k78Bxz_9Ort4nWgM-t6c7V-1tC9q_oYsj6ONltQgI6cyvlDrOexPH3AZUeSKxwJI"
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