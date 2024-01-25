package com.example.playlistmaker.ui.media.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.data.model.Playlist
import com.example.playlistmaker.databinding.PlaylistFragmentBinding
import com.example.playlistmaker.ui.common.PlaylistAdapter
import com.example.playlistmaker.ui.media.view_model.PlaylistFragmentViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private var _binding: PlaylistFragmentBinding? = null
    private val binding get() = _binding!!

    private val playlistFragmentViewModel by viewModel<PlaylistFragmentViewModel>()

    private lateinit var adapter: PlaylistAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlaylistFragmentBinding.inflate(inflater, container, false)
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
            fragmentActivity.findViewById<BottomNavigationView>(R.id.bottomNavigationView).setVisibility(View.VISIBLE);
        }


        binding.addPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_playlistAddFragment2)
        }
        playlistFragmentViewModel.getPlaylists()

        playlistFragmentViewModel.playlistList.observe(viewLifecycleOwner) { it ->
            setElement(it)
        }



    }

    private fun setElement(plList: List<Playlist>) {
        if (plList.isNullOrEmpty()) {
            binding.playlistEmptyImage.visibility = View.VISIBLE
            binding.playlistEmptyText.visibility = View.VISIBLE
            binding.playlistRecycleView.visibility = View.GONE
        } else {
            binding.playlistEmptyImage.visibility = View.GONE
            binding.playlistEmptyText.visibility = View.GONE
            adapter = PlaylistAdapter(plList, requireContext())
            binding.playlistRecycleView.layoutManager = GridLayoutManager(requireContext(), 2)
            binding.playlistRecycleView.adapter = adapter
            adapter.notifyDataSetChanged()
            binding.playlistRecycleView.visibility = View.VISIBLE

        }
    }

    fun onPlaylistClick() {

    }
    companion object {
        fun newInstance() = PlaylistFragment().apply { }
    }

}
