package com.raq.violentclock.`interface`

import com.raq.violentclock.data.Devices
import com.raq.violentclock.data.SpotifyPostSong
import com.raq.violentclock.data.Tracks
import retrofit2.Call
import retrofit2.http.*

private const val TOKEN = "Bearer BQAm0Uxv7nshZPK873cFvxCR2Z_yu01N_VDdYbtO_9U626WKFue64lIHoW_zLCkTlg2v8z7b7OMvjgVCIWV7KZA7P59I4gUesteeffQdVKLyoL7F0_oJbJIjk3De4Kbi0bxBuv0crQbKdKmeQsWHbqMi"
private const val SEARCH_TYPE = "track"

interface SpotifyInterface {
    @GET("/v1/search")
    fun findSongByName(
        @Header("Authorization")
        header: String = TOKEN,
        @Query("q")
        search: String,
        @Query("type")
        type: String = SEARCH_TYPE
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