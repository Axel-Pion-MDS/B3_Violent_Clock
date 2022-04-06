package com.raq.violentclock.`interface`

import com.raq.violentclock.data.Tracks
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

private const val TOKEN = "BQCNgfL0HjEnmf2S0kKrUpkfX_Usj1brlJV8R_54RNmHguw2T_CYWCYY4AEJCyKMxUoJ3JrCC9EfOf8ET39iQv5Br_fgWzAJkl3IdmDj5qGWJxWwI4xgZ6A6d_daowR38nyIJ_jvswC8fOo"
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