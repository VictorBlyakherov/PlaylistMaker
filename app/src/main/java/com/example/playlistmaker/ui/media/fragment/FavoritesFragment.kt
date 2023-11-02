package com.example.playlistmaker.ui.media.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FavoritesFragmentBinding
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.ui.media.view_model.FavoritesFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FavoritesFragment: Fragment() {
    private lateinit var binding: FavoritesFragmentBinding
    private val favoritesViewModel: FavoritesFragmentViewModel by viewModel {
        parametersOf()
    }


    companion object {
        fun newInstance() = FavoritesFragment().apply {  }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FavoritesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
     }
}