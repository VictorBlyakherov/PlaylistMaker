package com.example.playlistmaker.ui.player.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackDetailsBinding
import com.example.playlistmaker.ui.player.view_model.TrackDetailViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class TrackDetailsActivity : AppCompatActivity() {


    private lateinit var trackDetailViewModel: TrackDetailViewModel

    private lateinit var binding: TrackDetailsBinding


    override fun onPause() {
        super.onPause()
        trackDetailViewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        trackDetailViewModel.stopPlayer()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TrackDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        trackDetailViewModel = ViewModelProvider(
            this,
            TrackDetailViewModel.getViewModelFactory()
        )[TrackDetailViewModel::class.java]



        binding.backTrackDetail.setOnClickListener {
            this.finish()
        }

        trackDetailViewModel.preparePlayer(intent)


        binding.play.setOnClickListener {
            trackDetailViewModel.playbackControl()
        }

        trackDetailViewModel.isPlayEnable.observe(this) { isEnable ->
            binding.play.isEnabled = isEnable
        }

        trackDetailViewModel.track.observe(this) {track ->
            fun getCoverArtwork() = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

            binding.artistName.text = track.artistName
            binding.trackName.text = track.trackName
            binding.trackDuration.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
            binding.trackAlbum.text = track.collectionName
            binding.trackYear.text = track.releaseDate.substring(0, 4)
            binding.trackGenre.text = track.primaryGenreName
            binding.trackCountry.text = track.country

            Glide.with(this)
                .load(getCoverArtwork())
                .placeholder(R.drawable.placeholder_big)
                .centerInside()
                .transform(RoundedCorners(10))
                .into(binding.trackCover)
        }


        trackDetailViewModel.playIcon.observe(this) {it ->
            if (it.equals("play")) {
                binding.play.setImageDrawable(getResources().getDrawable(R.drawable.play))
            } else if (it.equals("pause")) {
                binding.play.setImageDrawable(getResources().getDrawable(R.drawable.pause))
            }
        }

        trackDetailViewModel.currentPlayPosition.observe(this) {currentPlayPosition ->
            val tempStr: String = if (currentPlayPosition < 10) {
                "00:0$currentPlayPosition"
            } else {
                "00:$currentPlayPosition"
            }
            binding.playDuration.text = tempStr

        }
    }

}