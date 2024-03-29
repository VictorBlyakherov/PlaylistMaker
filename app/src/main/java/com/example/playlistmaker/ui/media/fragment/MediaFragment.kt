package com.example.playlistmaker.ui.media.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaBinding
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.ui.media.activity.MediaPagerAdapter
import com.example.playlistmaker.ui.media.view_model.FavoritesFragmentViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaFragment: Fragment() {

    private var _binding: FragmentMediaBinding? = null

    private val binding get() = _binding!!

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMediaBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentActivity = getActivity();

        if (fragmentActivity != null) {
            fragmentActivity.findViewById<BottomNavigationView>(R.id.bottomNavigationView).setVisibility(View.VISIBLE);
        }


        binding.viewPager.adapter = MediaPagerAdapter(
            fragmentManager = childFragmentManager,
            lifecycle
        )

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorites_str)
                1 -> tab.text = getString(R.string.playlist_str)
            }
        }
        tabMediator.attach()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
        _binding = null
    }


}