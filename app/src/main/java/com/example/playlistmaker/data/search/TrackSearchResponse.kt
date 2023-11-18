package com.example.playlistmaker.data.search

import com.example.playlistmaker.data.search.dto.Response
import com.example.playlistmaker.domain.model.Track

class TrackSearchResponse(val results: List<Track>) : Response() {


}