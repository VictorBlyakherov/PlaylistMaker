package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.SettingsActivityBinding
import com.example.playlistmaker.databinding.TrackDetailsBinding
import java.text.SimpleDateFormat
import java.util.Locale

class TrackDetailsActivity : AppCompatActivity() {

    private lateinit var binding: TrackDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TrackDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.backTrackDetail.setOnClickListener {
            this.finish()
        }


        val track = intent.getSerializableExtra("track") as? Track

        fun getCoverArtwork() = track?.artworkUrl100?.replaceAfterLast('/',"512x512bb.jpg")

        binding.artistName.text = track?.artistName
        binding.trackName.text = track?.trackName
        binding.playDuration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track?.trackTimeMillis)
        binding.trackAlbum.text = track?.collectionName
        binding.trackYear.text = track?.releaseDate
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