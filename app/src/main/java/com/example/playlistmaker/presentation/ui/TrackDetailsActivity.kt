package com.example.playlistmaker.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.model.PlayerState
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.databinding.TrackDetailsBinding
import com.example.playlistmaker.domain.api.PlayTrackInteractor
import java.text.SimpleDateFormat
import java.util.Locale

class TrackDetailsActivity : AppCompatActivity() {

    companion object {
        private const val PLAY_DEBOUNCE_DELAY = 1000L
    }

    private var currentPlayPosition: Int = 0

    private val handler = Handler(Looper.getMainLooper())

    private val interactor: PlayTrackInteractor = Creator.providePlayTrackInteractor()

    private lateinit var binding: TrackDetailsBinding

    private val timeChangeRunnable = increasePlayTime()



    private fun increasePlayTime() :Runnable {

        return object : Runnable {
            override fun run() {
                currentPlayPosition++
                val tempStr: String = if (currentPlayPosition < 10) {
                    "00:0$currentPlayPosition"
                } else {
                    "00:$currentPlayPosition"
                }
                binding.playDuration.text = tempStr
                handler.postDelayed(this, PLAY_DEBOUNCE_DELAY)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        interactor.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        interactor.stopPlayer()
    }

    private fun playbackControl() {
        when (interactor.getPlayerState()) {
            PlayerState.STATE_PAUSED, PlayerState.STATE_PREPARED, PlayerState.STATE_DEFAULT -> {
                handler.removeCallbacks(timeChangeRunnable)
                setPauseIcon()
                interactor.startPlayer()
                handler.postDelayed(timeChangeRunnable, PLAY_DEBOUNCE_DELAY)
            }

            PlayerState.STATE_PLAYING -> {
                handler.removeCallbacks(timeChangeRunnable)
                setPlayIcon()
                interactor.pausePlayer()
            }
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TrackDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.backTrackDetail.setOnClickListener {
            this.finish()
        }


        val track = intent.getSerializableExtra("track") as? Track
        var trackUrl = ""
        trackUrl = if (track?.previewUrl == null) {
            ""
        } else {
            track.previewUrl
        }

        preparePlayer(trackUrl)


        binding.play.setOnClickListener {
            playbackControl()
        }



        fun getCoverArtwork() = track?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

        binding.artistName.text = track?.artistName
        binding.trackName.text = track?.trackName
        binding.trackDuration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track?.trackTimeMillis)
        binding.trackAlbum.text = track?.collectionName
        binding.trackYear.text = track?.releaseDate?.substring(0, 4)
        binding.trackGenre.text = track?.primaryGenreName
        binding.trackCountry.text = track?.country

        Glide.with(this)
            .load(getCoverArtwork())
            .placeholder(R.drawable.placeholder_big)
            .centerInside()
            .transform(RoundedCorners(10))
            .into(binding.trackCover)

    }

    private fun preparePlayer(url: String) {
        interactor.preparePlayer(url)
        handler.removeCallbacks(timeChangeRunnable)
        setPlayIcon()
        binding.play.isEnabled = true
        currentPlayPosition = 0
        getTrackOnCompletionListener()
    }

    private fun getTrackOnCompletionListener() {
        interactor.setTrackCompletionListener {
            setPlayIcon()
            currentPlayPosition = 0
            handler.removeCallbacks(timeChangeRunnable)
            binding.playDuration.text = "00:00"
        }
    }

    private fun setPlayIcon() {
        binding.play.setImageDrawable(getResources().getDrawable(R.drawable.play))
    }

    private fun setPauseIcon() {
        binding.play.setImageDrawable(getResources().getDrawable(R.drawable.pause))
    }

}