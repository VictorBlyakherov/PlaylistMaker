package com.example.playlistmaker.ui.media.fragment

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FavoritesFragmentBinding
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.domain.model.FavoritesState
import com.example.playlistmaker.ui.common.TrackAdapter
import com.example.playlistmaker.ui.media.view_model.FavoritesFragmentViewModel
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

        favoritesFragmentViewModel.getFavoritesList()

        favoritesFragmentViewModel.favoritesStatus.observe(viewLifecycleOwner) { it ->

            val trackList = favoritesFragmentViewModel.getFavoritesList().value

            adapter = TrackAdapter({
                onTrackClick(it)
            }, trackList!!)

            binding.favoritesList.layoutManager = LinearLayoutManager(requireContext())
            binding.favoritesList.adapter = adapter
            adapter.notifyDataSetChanged()
            setElement(it)
        }



    }

    companion object {
        fun newInstance() = FavoritesFragment().apply { }
    }

    fun onTrackClick(trackId: Int) {
        if (clickDebounce()) {
            favoritesFragmentViewModel.clickTrack(requireContext(), trackId)
        }
    }

    private fun clickDebounce(): Boolean {

        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun setElement(status: FavoritesState) {
        if (status == FavoritesState.EMPTY_RESULT) {
            binding.favoritesEmptyLayout.visibility = View.VISIBLE
            binding.favoritesLayout.visibility = View.INVISIBLE
        } else if (status == FavoritesState.SUCCESS) {
            binding.favoritesEmptyLayout.visibility = View.INVISIBLE
            binding.favoritesLayout.visibility = View.VISIBLE
        }
    }

}
