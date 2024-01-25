package com.example.playlistmaker.ui.common

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.data.model.Playlist
import com.example.playlistmaker.data.model.Track

class PlaylistAdapter (private val playlists: List<Playlist>, context_: Context
) : RecyclerView.Adapter<PlaylistViewHolder>() {

    private val context: Context

    init {
        context = context_
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_list, parent, false)
        return PlaylistViewHolder(view, context)

    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }


    override fun getItemCount() = playlists.size

    fun interface ClickPlaylistListener {
        fun onPlaylistClick(playlistName: String)
    }

}