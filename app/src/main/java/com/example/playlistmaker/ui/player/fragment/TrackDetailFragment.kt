package com.example.playlistmaker.ui.player.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.data.model.Playlist
import com.example.playlistmaker.databinding.FragmentAddPlaylistBinding
import com.example.playlistmaker.databinding.TrackDetailsBinding
import com.example.playlistmaker.domain.model.PlayingStatus
import com.example.playlistmaker.ui.common.PlaylistShortAdapter
import com.example.playlistmaker.ui.common.TrackDetailStorage
import com.example.playlistmaker.ui.media.view_model.PlaylistAddFragmentViewModel
import com.example.playlistmaker.ui.player.view_model.TrackDetailViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class TrackDetailFragment: Fragment() {

    private var _binding: TrackDetailsBinding? = null

    private val binding get() = _binding!!

    private val trackDetailViewModel by viewModel<TrackDetailViewModel>()

    private lateinit var adapter: PlaylistShortAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = TrackDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        trackDetailViewModel.stopPlayer()
        _binding = null
    }


    override fun onPause() {
        super.onPause()
        trackDetailViewModel.pausePlayer()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val fragmentActivity = getActivity();

        if (fragmentActivity != null) {
            fragmentActivity.findViewById<BottomNavigationView>(R.id.bottomNavigationView).setVisibility(View.GONE);
        }

        binding.backTrackDetail.setOnClickListener {
            findNavController().popBackStack()

        }

        trackDetailViewModel.preparePlayer((requireActivity() as TrackDetailStorage).getTrackDetail())


        binding.play.setOnClickListener {
            trackDetailViewModel.playbackControl()
        }

        binding.addToFavorites.setOnClickListener {
            trackDetailViewModel.setFavorites()
        }


        binding.addPlaylistShortButton.setOnClickListener {
            findNavController().navigate(R.id.action_trackDetailFragment_to_playlistAddFragment)
        }



        trackDetailViewModel.track.observe(viewLifecycleOwner) { track ->

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
        }

        trackDetailViewModel._isInFavorites.observe(viewLifecycleOwner) {
            setFavoritesButton(it)
        }

        trackDetailViewModel._addedToPlaylist.observe(viewLifecycleOwner) {
            if (it.first) {
                val bottomSheetBehavior = BottomSheetBehavior.from(binding.addToPlaylistBottomSheet)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }

            Toast.makeText(requireContext(), it.second, Toast.LENGTH_LONG).show()
        }


        trackDetailViewModel.playingStatus.observe(viewLifecycleOwner) { it ->
            binding.play.isEnabled = true
            if (it == PlayingStatus.PLAY) {
                binding.play.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.play))
            } else if (it == PlayingStatus.PAUSE) {
                binding.play.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pause))
            }
        }

        trackDetailViewModel.currentPlayPosition.observe(viewLifecycleOwner) { currentPlayPosition ->
            binding.playDuration.text = currentPlayPosition

        }
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.addToPlaylistBottomSheet)

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN


        binding.addToPlayList.setOnClickListener {
            trackDetailViewModel.getPlaylists()

            setElement(trackDetailViewModel.playlistList.value!!)

            val bottomSheetBehavior = BottomSheetBehavior.from(binding.addToPlaylistBottomSheet)

            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        }

        trackDetailViewModel.getPlaylists()

        trackDetailViewModel.playlistList.observe(viewLifecycleOwner) { it ->
            setElement(it)
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset
            }

        })



    }

    private fun setElement(plList: List<Playlist>) {
        if (!plList.isNullOrEmpty()) {
            adapter = PlaylistShortAdapter(plList, requireContext(), {onPlaylistClick(it)})
            binding.playlistShortRecycleView.layoutManager = LinearLayoutManager(requireContext())
            binding.playlistShortRecycleView.adapter = adapter
            adapter.notifyDataSetChanged()
            binding.playlistShortRecycleView.visibility = View.VISIBLE

        }
    }

    fun setFavoritesButton(isInFavorites: Boolean){

        if (isInFavorites) {
            binding.addToFavorites.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.remove_from_favorites))
        } else {
            binding.addToFavorites.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.add_to_favorites))
        }
    }


    fun onPlaylistClick(playlist: Playlist) {
        trackDetailViewModel.addToPlaylist(playlist)
    }


}