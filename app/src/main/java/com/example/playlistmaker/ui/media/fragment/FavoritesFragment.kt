package com.example.playlistmaker.ui.media.fragment

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FavoritesFragmentBinding
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.domain.model.FavoritesState
import com.example.playlistmaker.ui.common.TrackAdapter
import com.example.playlistmaker.ui.common.TrackDetailStorage
import com.example.playlistmaker.ui.media.view_model.FavoritesFragmentViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FavoritesFragment : Fragment() {
    private lateinit var binding: FavoritesFragmentBinding

    private val favoritesFragmentViewModel by viewModel<FavoritesFragmentViewModel>()

    private lateinit var adapter: TrackAdapter

    private var isClickAllowed = true

    private val CLICK_DEBOUNCE_DELAY = 1000L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FavoritesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentActivity = getActivity();

        if (fragmentActivity != null) {
            fragmentActivity.findViewById<BottomNavigationView>(R.id.bottomNavigationView).setVisibility(View.VISIBLE);
        }


        favoritesFragmentViewModel.fillFavoritesList()

        favoritesFragmentViewModel.favoritesStatus.observe(viewLifecycleOwner) { it ->
            setElement(it)
        }

    }

    companion object {
        fun newInstance() = FavoritesFragment().apply { }
    }

    fun onTrackClick(trackId: Int) {
        if (favoritesFragmentViewModel.clickDebounce()) {
            val track = favoritesFragmentViewModel.clickTrack(trackId)
            (requireActivity() as TrackDetailStorage).setTrackDetail(track)
            findNavController().navigate(R.id.action_mediaFragment_to_trackDetailFragment)
        }
    }


    private fun setElement(status: FavoritesState) {
        if (status == FavoritesState.EMPTY_RESULT) {
            binding.favoritesEmptyLayout.visibility = View.VISIBLE
            binding.favoritesLayout.visibility = View.INVISIBLE
        } else if (status == FavoritesState.SUCCESS) {
            val trackList = favoritesFragmentViewModel._favoriteList.value

            adapter = TrackAdapter({
                onTrackClick(it)
            }, trackList!!)

            binding.favoritesList.layoutManager = LinearLayoutManager(requireContext())
            binding.favoritesList.adapter = adapter
            adapter.notifyDataSetChanged()
            binding.favoritesEmptyLayout.visibility = View.INVISIBLE
            binding.favoritesLayout.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        favoritesFragmentViewModel.fillFavoritesList()
    }

}
