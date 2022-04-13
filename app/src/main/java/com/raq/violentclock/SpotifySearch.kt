package com.raq.violentclock

import android.util.Log
import android.widget.EditText
import com.raq.violentclock.data.*
import com.raq.violentclock.service.MusicService

class SpotifySearch {
    private var dataList = ArrayList<SpotifyData>()
    private var listOfArtists = ArrayList<Artists>()
    private var listOfImages = ArrayList<Images>()
    private var result = ArrayList<String>()

    fun normalize(arrayList: Items): String {
        dataList.clear()
        result.clear()
        for (data in arrayList.items) {
            dataList.add(data.album)

            listOfImages.clear()
            listOfArtists.clear()
            for (image in data.album.images!!) {
                listOfImages.add(image)
            }

            for (artist in data.album.artists) {
                listOfArtists.add(artist)
            }

            result.add(data.album.id)
            result.add(data.album.href)
            result.add(data.album.name)
            result.add(dataList.toString())
            result.add(listOfArtists.toString())
            result.add(listOfImages.toString())
        }

        return result.toString()
    }
}