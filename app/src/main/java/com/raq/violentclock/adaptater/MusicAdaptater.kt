package com.raq.violentclock.adaptater

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.raq.violentclock.R
import com.raq.violentclock.data.SpotifyData
import com.squareup.picasso.Picasso

class MusicAdaptater (private val context: Activity, private val musics: List<SpotifyData>, private val listOfMusicsName: List<String>)
    : ArrayAdapter<String>(context, R.layout.listview_item_music, listOfMusicsName) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.listview_item_alarm, null, true)

        val musicName : TextView = rowView.findViewById(R.id.musicName)
        val musicArtist : TextView = rowView.findViewById(R.id.musicArtist)
        val musicCover : ImageView = rowView.findViewById(R.id.musicCover)

        if (musics[position].images != null) {
            Picasso.get().load(musics[position].images?.get(0)?.url).into(musicCover);
        } else {
            Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/f/f5/No_Cross.svg/1200px-No_Cross.svg.png").into(musicCover);
        }
        musicName.text = musics[position].name
        musicArtist.text = musics[position].artists?.get(0)?.name
        Picasso.get().load(musics[position].images?.get(0)?.url).into(musicCover)

        return rowView
    }
}