package com.example.playlistmaker.ui.media.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAddPlaylistBinding
import com.example.playlistmaker.ui.media.view_model.PlaylistAddFragmentViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel


open class PlaylistAddFragment: Fragment() {

    private var _binding: FragmentAddPlaylistBinding? = null

    protected open val binding get() = _binding!!

    private var imageChanged = false

    protected open val playlistAddFragmentViewModel by viewModel<PlaylistAddFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddPlaylistBinding.inflate(inflater, container, false)
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
            fragmentActivity.findViewById<BottomNavigationView>(R.id.bottomNavigationView).setVisibility(View.GONE);
        }


        setAddButton()

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Do custom work here
                    showDialog()
                    }
                }
            )


        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setAddButton()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        binding.nameEditText.addTextChangedListener(simpleTextWatcher)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.playlistCover.setImageURI(uri)
                    imageChanged = true
                    playlistAddFragmentViewModel.saveImageToPrivateStorage(uri, requireContext())
                }
            }
        binding.playlistCover.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.addButton.setOnClickListener {
            playlistAddFragmentViewModel.addPlaylist(
                binding.nameEditText.text.toString(),
                binding.descEditText.text.toString()
            )
            Toast.makeText(requireContext(), "Плейлист " + binding.nameEditText.text.toString() + " создан", Toast.LENGTH_LONG).show()
            findNavController().popBackStack()
        }

        binding.backTrackDetail.setOnClickListener {
            showDialog()
        }

    }


    companion object {
        fun newInstance() = PlaylistAddFragment()
    }

    private fun setAddButton() {
        binding.addButton.isEnabled = binding.nameEditText.text.toString().trim().isNotEmpty()
    }

    private fun showDialog() {

        if (binding.nameEditText.text.toString().isNotEmpty()
            or binding.descEditText.text.toString().isNotEmpty()
            or imageChanged
        )
        {

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.playlist_end))
                .setMessage(getString(R.string.all_resources_will_be_lost))
                .setNeutralButton(getString(R.string.cancel)) { dialog, which ->

                }
                .setNegativeButton(getString(R.string.finish)) { dialog, which ->
                    findNavController().popBackStack()
                }
                .show()
        }
        else {
            findNavController().popBackStack()
        }
    }
}