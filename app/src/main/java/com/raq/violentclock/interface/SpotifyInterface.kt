package com.raq.violentclock.`interface`

import com.raq.violentclock.data.Tracks
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

private const val TOKEN = "  BQCBf3_vv8x7vJeHgUu7kzNQRZ0OSQSA75wuVSsH04OqWP75IA6oJYE55jhUuDrDEVU_CdY70ve0zzE8nATEs_y1vD0uAxEOW8UiXGu8b5DVF8W6-tZXVAJM_4jn2yEW1FGdODxktQHXhvw"
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
}