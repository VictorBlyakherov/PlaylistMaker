package com.example.playlistmaker.ui.player.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackDetailsBinding
import com.example.playlistmaker.domain.model.PlayingStatus
import com.example.playlistmaker.ui.player.view_model.TrackDetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class TrackDetailsActivity : AppCompatActivity() {


    private val trackDetailViewModel by viewModel<TrackDetailViewModel>()

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

        binding.backTrackDetail.setOnClickListener {
            this.finish()
        }

        trackDetailViewModel.preparePlayer(intent)


        binding.play.setOnClickListener {
            trackDetailViewModel.playbackControl()
        }

        binding.addToFavorites.setOnClickListener {
            trackDetailViewModel.setFavorites()
        }



        trackDetailViewModel.track.observe(this) { track ->
            Log.d("AAAA", "5")

            fun getCoverArtwork() = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

            binding.artistName.text = track.artistName
            binding.trackName.text = track.trackName
            binding.trackDuration.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
            binding.trackAlbum.text = track.collectionName
            binding.trackYear.text = if (track.releaseDate.length > 4) track.releaseDate.substring(
                0, 4
            ) else track.releaseDate
            binding.trackGenre.text = track.primaryGenreName
            binding.trackCountry.text = track.country

            Glide.with(this).load(getCoverArtwork()).placeholder(R.drawable.placeholder_big)
                .centerInside().transform(RoundedCorners(10)).into(binding.trackCover)
            setFavoritesButton(track.isInFavorites)

        }


        trackDetailViewModel.playingStatus.observe(this) { it ->
            binding.play.isEnabled = true
            if (it == PlayingStatus.PLAY) {
                binding.play.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.play))
            } else if (it == PlayingStatus.PAUSE) {
                binding.play.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pause))
            }
        }

        trackDetailViewModel.currentPlayPosition.observe(this) { currentPlayPosition ->
            binding.playDuration.text = currentPlayPosition

        }
    }

    fun setFavoritesButton(isInFavorites: Boolean){
        Log.d("AAAA", "6")

        if (isInFavorites) {
            binding.addToFavorites.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.remove_from_favorites))
        } else {
            binding.addToFavorites.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.add_to_favorites))
        }
    }

}