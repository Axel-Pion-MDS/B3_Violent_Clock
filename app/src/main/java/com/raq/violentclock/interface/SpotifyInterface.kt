package com.raq.violentclock.`interface`

import android.widget.EditText
import com.raq.violentclock.data.Devices
import com.raq.violentclock.data.SpotifyPostSong
import com.raq.violentclock.data.Tracks
import retrofit2.Call
import retrofit2.http.*

private const val TOKEN = "Bearer BQAaT69VsmpebB8mauCzHeSOCbb1Xwz-h-G0RGDJjQYPLRX6kXMCE9Hewx-Es3M07yiCN8xHn1eP0_XmGhTy55Y6A4RciQpTt-wtxDdvNemGI6aGSLbKxairCEyI7Wy4LbRdewXpgKM3X4XxSlNUJECZXzYsf_yMP1g"
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