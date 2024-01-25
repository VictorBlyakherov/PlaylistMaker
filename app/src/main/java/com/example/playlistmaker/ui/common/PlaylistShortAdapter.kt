package com.example.playlistmaker.ui.common

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.data.model.Playlist

class PlaylistShortAdapter (private val playlists: List<Playlist>, context_: Context, private val clickPlaylistListener: ClickPlaylistListener
) : RecyclerView.Adapter<PlaylistShortViewHolder>() {

    private val context: Context

    init {
        context = context_
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistShortViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_short_list, parent, false)
        return PlaylistShortViewHolder(view, context)

    }

    override fun onBindViewHolder(holder: PlaylistShortViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            clickPlaylistListener.onPlaylistClick(playlists[position])
        }
    }


    override fun getItemCount() = playlists.size

    fun interface ClickPlaylistListener {
        fun onPlaylistClick(playlist: Playlist)
    }

}