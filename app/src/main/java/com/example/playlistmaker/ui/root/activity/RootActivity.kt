package com.example.playlistmaker.ui.root.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.data.model.Playlist
import com.example.playlistmaker.data.model.Track
import com.example.playlistmaker.databinding.ActivityRootBinding
import com.example.playlistmaker.ui.common.PlaylistDetailStorage
import com.example.playlistmaker.ui.common.TrackDetailStorage
import java.util.Locale


class RootActivity : AppCompatActivity(), TrackDetailStorage, PlaylistDetailStorage {

    private lateinit var binding: ActivityRootBinding

    private lateinit var trackDetail: Track

    private lateinit var playlistDetail: Playlist


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(updateLocale(base))
        applyOverrideConfiguration(base.resources.configuration)
    }

    private fun updateLocale(context: Context): Context? {
        val ruLocale = Locale("ru")
        Locale.setDefault(ruLocale)
        val configuration: Configuration = context.resources.configuration
        configuration.setLocale(ruLocale)
        configuration.setLayoutDirection(ruLocale)
        return context.createConfigurationContext(configuration)
    }


    override fun setTrackDetail(trackDetailArg: Track) {
        trackDetail = trackDetailArg
    }

    override fun getTrackDetail(): Track {
        return trackDetail
    }

    override fun setPlaylistDetail(playlistDetailArg: Playlist) {
        playlistDetail = playlistDetailArg
    }

    override fun getPlaylistDetail(): Playlist {
        return playlistDetail
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setupWithNavController(navController)

    }

}