package com.example.playlistmaker

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.TrackDetailsBinding
import java.text.SimpleDateFormat
import java.util.Locale

class TrackDetailsActivity : AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val PLAY_DEBOUNCE_DELAY = 1000L
    }

    private var playerState = STATE_DEFAULT

    private val handler = Handler(Looper.getMainLooper())

    private var currentPlayPosition: Int = 0

    private val mediaPlayer = MediaPlayer()

    private lateinit var binding: TrackDetailsBinding

    private val timeChangeRunnable = Runnable { increasePlayTime() }

    private fun startPlayer() {
        mediaPlayer.start()
        binding.play.setImageDrawable(getResources().getDrawable(R.drawable.pause))
        playerState = STATE_PLAYING
        handler.postDelayed(timeChangeRunnable, PLAY_DEBOUNCE_DELAY)

        mediaPlayer.setOnCompletionListener {
            binding.play.setImageDrawable(getResources().getDrawable(R.drawable.play))
            playerState = STATE_PREPARED
            handler.removeCallbacks(timeChangeRunnable)
            binding.playDuration.text = "00:00"
            currentPlayPosition = 0
        }
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.play.setImageDrawable(getResources().getDrawable(R.drawable.play))
        playerState = STATE_PAUSED
        handler.removeCallbacksAndMessages(timeChangeRunnable)
    }

    private fun increasePlayTime() {
        if (playerState == STATE_PLAYING) {
            currentPlayPosition++
            val tempStr: String = if (currentPlayPosition < 10) {
                "00:0$currentPlayPosition"
            } else {
                "00:$currentPlayPosition"
            }

            binding.playDuration.text = tempStr
            handler.postDelayed({ increasePlayTime() }, PLAY_DEBOUNCE_DELAY)
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
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

        mediaPlayer.setDataSource(track?.previewUrl)
        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener {
            binding.play.isEnabled = true
            playerState = STATE_PREPARED
        }





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
}