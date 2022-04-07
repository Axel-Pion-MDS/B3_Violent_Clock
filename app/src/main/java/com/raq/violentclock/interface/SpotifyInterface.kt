package com.raq.violentclock.`interface`

import com.raq.violentclock.data.Tracks
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

<<<<<<< Updated upstream
private const val TOKEN = "Bearer BQDJ28Jqp1gruEHW9dprr9z27TAl7lPSO_28srAjsrD_fxy7CLLaczMz3eOsbN244ZngrDLyuwBsnAapi-NA5lKaDi-K-onWmZtixkLxebbOGdtLaE-c2yqdNunknMkLAiHbJ0cu-X-HxpRBnj4"
=======
private const val TOKEN = "Bearer BQDpsdVDCakpFeBqz4x3RCjkrQjtuZkPwfZPvR32UGNTTMxIQnC9qIoEEpiM7xUsNKPaET8AcHIA-oZ61qOXDRi0T0pRhTW7n1tK8QgHdfSxpZGH4ueYtJFE9sSGFPN6chKteOD3cnDjf_s"
>>>>>>> Stashed changes
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