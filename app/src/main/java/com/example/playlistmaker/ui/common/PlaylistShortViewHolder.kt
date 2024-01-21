package com.example.playlistmaker.ui.common

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.data.model.Playlist
import java.io.File

class PlaylistShortViewHolder (itemView: View, context_: Context) : RecyclerView.ViewHolder(itemView) {
    private val playlistCoverView: ImageView
    private val playlistNameView: TextView
    private val playlistTrackCountView: TextView
    private val context: Context
    private val utils: Utils = Utils()

    init {
        playlistCoverView = itemView.findViewById(R.id.playlistShortCover)
        playlistNameView = itemView.findViewById(R.id.playlistShortName)
        playlistTrackCountView = itemView.findViewById(R.id.playlistShortTrackCount)
        context = context_
    }

    fun bind(model: Playlist) {
        playlistNameView.text = model.playlistName
        playlistTrackCountView.text = utils.getTrackCountString(model.trackCount)
        val image = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "covers/" + model.coverFileName
        )
        if (!image.exists() or model.coverFileName.isEmpty()) {
            playlistCoverView.setImageResource(R.drawable.placeholder_big)
        } else {
            val bm = BitmapFactory.decodeFile(image.absolutePath)
            playlistCoverView.setImageBitmap(bm)
        }
    }



}