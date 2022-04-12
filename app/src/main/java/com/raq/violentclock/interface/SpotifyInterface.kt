package com.raq.violentclock.`interface`

import android.widget.EditText
import com.raq.violentclock.data.Devices
import com.raq.violentclock.data.SpotifyPostSong
import com.raq.violentclock.data.Tracks
import retrofit2.Call
import retrofit2.http.*

private const val TOKEN = "Bearer BQCDlOQ9TTgo7Pljd2MI0WhIcDs03hum4kKGAYM8F4sE37ZvStiOOyt3qi0EgPBRvGJMdPkDrv_5KHEMqme7TpANcY_Fu2uLr_CJXOE0QAtQg7e735XbHhEf0Vu-9_VPWaHqjyjlVmzlE1M49F_A2u0rSyud"
private const val SEARCH_TYPE = "track"

interface SpotifyInterface {
    @GET("/v1/search")
    fun findSongByName(
        @Header("Authorization")
        header: String = TOKEN,
        @Query("q")
        search: EditText,
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