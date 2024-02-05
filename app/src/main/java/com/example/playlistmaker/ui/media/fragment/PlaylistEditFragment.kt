package com.example.playlistmaker.ui.media.fragment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.data.model.Playlist
import com.example.playlistmaker.ui.common.PlaylistDetailStorage
import com.example.playlistmaker.ui.media.view_model.PlaylistEditFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class PlaylistEditFragment: PlaylistAddFragment() {

    override val playlistAddFragmentViewModel by viewModel<PlaylistEditFragmentViewModel>()

    private lateinit var playlist: Playlist


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlist = (requireActivity() as PlaylistDetailStorage).getPlaylistDetail()
        setElement()

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Do custom work here
                    findNavController().popBackStack()
                }
            }
        )

        binding.backTrackDetail.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.addButton.setOnClickListener {
            playlist.playlistName = binding.nameEditText.text.toString()
            playlist.playlistDescription = binding.descEditText.text.toString()
            playlistAddFragmentViewModel.savePlaylist(playlist)
            findNavController().popBackStack()
        }


    }

    private fun setElement() {
        binding.playlistAddTitle.text = getString(R.string.playlist_edit)
        binding.nameEditText.setText(playlist.playlistName)
        binding.descEditText.setText(playlist.playlistDescription)
        binding.addButton.text = getString(R.string.save)

        val image = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "covers/" + playlist.coverFileName
        )
        if (!image.exists() or playlist.coverFileName.isEmpty()) {
            binding.playlistCover.setImageResource(R.drawable.placeholder_big)
        } else {
            val bm = BitmapFactory.decodeFile(image.absolutePath)
            binding.playlistCover.setImageBitmap(bm)
        }
    }

}