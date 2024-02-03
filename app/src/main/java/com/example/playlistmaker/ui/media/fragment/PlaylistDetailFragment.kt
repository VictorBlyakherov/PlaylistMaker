package com.example.playlistmaker.ui.media.fragment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.data.model.Playlist
import com.example.playlistmaker.data.model.Track
import com.example.playlistmaker.databinding.PlaylistDetailFragmentBinding
import com.example.playlistmaker.ui.common.PlaylistDetailStorage
import com.example.playlistmaker.ui.common.PlaylistTrackAdapter
import com.example.playlistmaker.ui.common.TrackDetailStorage
import com.example.playlistmaker.ui.media.view_model.PlaylistDetailFragmentViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale


class PlaylistDetailFragment: Fragment() {

    private val FIX_ELEMENTS_HEIGHT_TRACKS = 550

    private val FIX_ELEMENTS_HEIGHT_MORE = 270

    private val MS_IN_MIN = 60000

    private var _binding: PlaylistDetailFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: PlaylistTrackAdapter

    private lateinit var playlist: Playlist

    private val playlistDetailFragmentViewModel by viewModel<PlaylistDetailFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = PlaylistDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentActivity = getActivity();

        if (fragmentActivity != null) {
            fragmentActivity.findViewById<BottomNavigationView>(R.id.bottomNavigationView).setVisibility(
                View.GONE);
        }

        val heightBM = resources.displayMetrics.heightPixels - resources.displayMetrics.widthPixels

        var behavior = BottomSheetBehavior.from(binding.playlistTrackListBottomSheet)
        behavior.peekHeight = heightBM - FIX_ELEMENTS_HEIGHT_TRACKS


        behavior = BottomSheetBehavior.from(binding.morePlaylistBottomSheet)
        behavior.peekHeight = heightBM - FIX_ELEMENTS_HEIGHT_MORE

        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

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


        playlist = (requireActivity() as PlaylistDetailStorage).getPlaylistDetail()
        playlistDetailFragmentViewModel.getTrackList(playlist)
        playlistDetailFragmentViewModel.trackList.observe(viewLifecycleOwner) {
            setElements(it)
        }

        binding.backPlaylistDetail.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.sharePlaylistTV.setOnClickListener {
            sharePlaylist()
        }

        binding.playlistShare.setOnClickListener {
            sharePlaylist()
        }

        binding.editPlaylistTV.setOnClickListener {
            findNavController().navigate(R.id.action_playlistDetailFragment_to_playlistEditFragment)
        }
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.morePlaylistBottomSheet)

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        binding.playlistMore.setOnClickListener {
            val bottomSheetBehavior = BottomSheetBehavior.from(binding.morePlaylistBottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.deletePlaylistTV.setOnClickListener {
            val str = "Хотите удалить плейлист \"" + playlist.playlistName + "\"?"
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("")
                .setMessage(str)
                .setNegativeButton(getString(R.string.no)) { dialog, which ->

                }
                .setPositiveButton(getString(R.string.yes)) { dialog, wich ->
                    playlistDetailFragmentViewModel.deletePlaylist(playlist)
                    findNavController().popBackStack()
                }
                .show()
        }

    }

    private fun sharePlaylist() {
        if (playlistDetailFragmentViewModel.trackList.value.isNullOrEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.no_tracks_in_playlist_toast), Toast.LENGTH_LONG).show()
        } else {
            var strShare = playlist.playlistName + "\n"
            if (!playlist.playlistDescription.isNullOrEmpty()) {
                strShare += playlist.playlistDescription + "\n"
            }

            strShare +=  resources.getQuantityString(
                R.plurals.plurals_tracks,
                playlist.trackCount,
                playlist.trackCount
            ) + "\n"

            var count = 1
            for (track in playlistDetailFragmentViewModel.trackList.value!!) {
                strShare += count.toString() + ". " + track.artistName + " - " + track.trackName + " (" + SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis) + ")\n"
                count++
            }

            playlistDetailFragmentViewModel.sharePlaylist(strShare)
        }

    }

    private fun setElements(trackList: List<Track>) {

        val duration = playlistDetailFragmentViewModel.getPlaylistTrackDuration() / MS_IN_MIN
        binding.playlistName.text = playlist.playlistName
        binding.playlistShortName.text = playlist.playlistName
        binding.playlistDesc.text = playlist.playlistDescription
        binding.playlistShortTrackCount.text = this.resources.getQuantityString(
            R.plurals.plurals_tracks,
            playlist.trackCount,
            playlist.trackCount
        );
        binding.durationTracks.text =
            this.resources.getQuantityString(R.plurals.plurals_min, duration.toInt(), duration);
        binding.countTracks.text = this.resources.getQuantityString(
            R.plurals.plurals_tracks,
            playlist.trackCount,
            playlist.trackCount
        );
        val image = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "covers/" + playlist.coverFileName
        )
        if (!image.exists() or playlist.coverFileName.isEmpty()) {
            binding.playlistCover.setImageResource(R.drawable.placeholder_big)
            binding.playlistShortCover.setImageResource(R.drawable.placeholder_big)
        } else {
            val bm = BitmapFactory.decodeFile(image.absolutePath)
            binding.playlistCover.setImageBitmap(bm)
            binding.playlistShortCover.setImageBitmap(bm)
        }



        if (trackList.isNullOrEmpty()) {
            binding.noTracks.visibility = View.VISIBLE
            binding.playlistTrackRecycleView.visibility = View.GONE

        } else {
            adapter = PlaylistTrackAdapter(
                { onTrackClick(it) },
                { onTrackLongClick(it) },
                trackList
            )

            binding.playlistTrackRecycleView.layoutManager = LinearLayoutManager(requireContext())
            binding.playlistTrackRecycleView.adapter = adapter
            adapter.notifyDataSetChanged()
            binding.noTracks.visibility = View.GONE
            binding.playlistTrackRecycleView.visibility = View.VISIBLE


        }
    }

    fun onTrackClick(trackId: Int) {
        val track = playlistDetailFragmentViewModel.clickTrack(trackId)
        (requireActivity() as TrackDetailStorage).setTrackDetail(track)
        findNavController().navigate(R.id.action_playlistDetailFragment_to_trackDetailFragment)
    }

    fun onTrackLongClick(trackId: Int): Boolean {
            val track = playlistDetailFragmentViewModel.clickTrack(trackId)
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("")
                .setMessage(getString(R.string.wanttodeletetrack))
                .setNegativeButton(getString(R.string.no)) { dialog, which ->

                }
                .setPositiveButton(getString(R.string.yes)) { dialog, wich ->
                    playlist = playlistDetailFragmentViewModel.deleteTrack(track, playlist)

                }
                .show()
        return true
    }
}