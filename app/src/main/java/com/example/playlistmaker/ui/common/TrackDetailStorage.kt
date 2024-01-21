package com.example.playlistmaker.ui.common

import com.example.playlistmaker.data.model.Track

interface TrackDetailStorage {

    fun setTrackDetail(trackDetailArg:Track)
    fun getTrackDetail():Track

}