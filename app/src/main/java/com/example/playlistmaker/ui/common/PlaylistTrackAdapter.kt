package com.example.playlistmaker.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.data.model.Track

class PlaylistTrackAdapter(  private val clickTrackListener: ClickTrackListener,
                             private val clickLongTrackListener: LongClickTrackListener,
private val tracks: List<Track>
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)

    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])

        holder.itemView.setOnLongClickListener {
            clickLongTrackListener.onTrackLongClick(tracks[position].trackId)
        }

        holder.itemView.setOnClickListener {
            clickTrackListener.onTrackClick(tracks[position].trackId)
        }

    }


    override fun getItemCount() = tracks.size

    fun interface ClickTrackListener {
        fun onTrackClick(trackId: Int)
    }

    fun interface LongClickTrackListener {
        fun onTrackLongClick(trackId: Int): Boolean
    }

}