package com.example.playlistmaker.ui.root.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.data.model.Track
import com.example.playlistmaker.databinding.ActivityRootBinding
import com.example.playlistmaker.ui.common.TrackDetailStorage
import com.example.playlistmaker.ui.media.fragment.MediaFragment
import com.example.playlistmaker.ui.search.fragment.SearchFragment
import com.example.playlistmaker.ui.settings.fragment.SettingsFragment

class RootActivity : AppCompatActivity(), TrackDetailStorage {

    private lateinit var binding: ActivityRootBinding

    private lateinit var trackDetail: Track

    override fun setTrackDetail(trackDetailArg: Track) {
        trackDetail = trackDetailArg
    }

    override fun getTrackDetail(): Track {
        return trackDetail
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