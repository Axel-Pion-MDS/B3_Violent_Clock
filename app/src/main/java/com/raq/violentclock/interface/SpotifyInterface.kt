package com.raq.violentclock.`interface`

import com.raq.violentclock.data.Devices
import com.raq.violentclock.data.SpotifyPostSong
import com.raq.violentclock.data.Tracks
import retrofit2.Call
import retrofit2.http.*

private const val TOKEN = "Bearer BQByUr8dikSSEemZXsk7ZC3Wfca4vyUDlIHPncSFGG-Ko4lmlMWjERNElmejnTdzevvf5LOnQHzkEp6uueD6TcTRjstZgKaR9EmlQ1XpojDt_vYlCGJCCZ3NTIhYZNKHKl7eDpmgV1i4ROXzsqhT0B14SUDEL4j-i2U"
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
        @Body()
        body: SpotifyPostSong
    ): Call<String>

}