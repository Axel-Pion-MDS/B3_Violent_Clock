package com.raq.violentclock.`interface`

import com.raq.violentclock.data.Tracks
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

private const val TOKEN = "Bearer BQDpsdVDCakpFeBqz4x3RCjkrQjtuZkPwfZPvR32UGNTTMxIQnC9qIoEEpiM7xUsNKPaET8AcHIA-oZ61qOXDRi0T0pRhTW7n1tK8QgHdfSxpZGH4ueYtJFE9sSGFPN6chKteOD3cnDjf_s"
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