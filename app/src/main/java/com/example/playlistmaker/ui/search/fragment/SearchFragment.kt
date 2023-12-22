package com.example.playlistmaker.ui.search.fragment

import android.os.Bundle

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.model.SearchStatuses
import com.example.playlistmaker.ui.common.TrackAdapter
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel by viewModel<SearchViewModel>()

    private val CLICK_DEBOUNCE_DELAY = 1000L

    private var isClickAllowed = true

    private var searchText: String = ""

    private lateinit var adapter: TrackAdapter

    private lateinit var adapterHistory: TrackAdapter


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

    private fun setElements(status: SearchStatuses) {
        if (status == SearchStatuses.SUCCESS) {
            binding.progressBar.visibility = View.GONE
            binding.technicalError.visibility = View.GONE
            binding.technicalErrorText.visibility = View.GONE
            binding.technicalErrorButton.visibility = View.GONE
            binding.trackList.visibility = View.VISIBLE
        } else if (status == SearchStatuses.CONNECTION_ERROR) {
            binding.progressBar.visibility = View.GONE
            binding.technicalError.setImageResource(R.drawable.technical_error)
            binding.technicalErrorText.setText(R.string.techError)
            binding.technicalError.visibility = View.VISIBLE
            binding.technicalErrorText.visibility = View.VISIBLE
            binding.technicalErrorButton.visibility = View.VISIBLE
            binding.trackList.visibility = View.GONE
        } else if (status == SearchStatuses.EMPTY_RESULT) {
            binding.progressBar.visibility = View.GONE
            binding.technicalError.setImageResource(R.drawable.not_found)
            binding.technicalErrorText.setText(R.string.notFound)
            binding.technicalError.visibility = View.VISIBLE
            binding.technicalErrorText.visibility = View.VISIBLE
            binding.technicalErrorButton.visibility = View.GONE
            binding.trackList.visibility = View.GONE
        } else if (status == SearchStatuses.IN_PROGRESS) {
            binding.progressBar.visibility = View.VISIBLE
            binding.technicalError.visibility = View.GONE
            binding.technicalErrorText.visibility = View.GONE
            binding.technicalErrorButton.visibility = View.GONE
            binding.trackList.visibility = View.GONE
        }
    }


    private fun searchTrack() {

        if (searchText.isNotEmpty()) {
            searchViewModel.searchTrack(searchText)
        }
    }


    override fun onPause() {
        super.onPause()
        searchViewModel.setPaused()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        binding.searchEditText.isSaveEnabled = false


        binding.clearIcon.setOnClickListener {
            searchViewModel.clearSearchText()
            binding.searchEditText.setText("")
        }



        searchViewModel.searchStatus.observe(viewLifecycleOwner) {
            setElements(it)
        }

        searchViewModel.getTrackList().observe(viewLifecycleOwner) { trackList ->
            adapter = TrackAdapter({
                onTrackClick(it)
            }, trackList)

            binding.trackList.layoutManager = LinearLayoutManager(requireContext())
            binding.trackList.adapter = adapter
            adapter.notifyDataSetChanged()
        }



        searchViewModel.getTrackHistoryList().observe(viewLifecycleOwner) { trackList ->
            adapterHistory = TrackAdapter({
                onTrackClick(it)
            }, trackList)

            binding.historyList.layoutManager = LinearLayoutManager(requireContext())
            binding.historyList.adapter = adapterHistory
            adapterHistory.notifyDataSetChanged()
        }

        searchViewModel.isShowHistoryList.observe(viewLifecycleOwner) {
            if (it) {
                showTrackHistory()
            } else {
                hideTrackHistory()
            }
        }

        searchViewModel.fillHistoryList()

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.visibility = clearButtonVisibility(s)
                searchText = s.toString()
                if (binding.searchEditText.hasFocus() && s.isNullOrEmpty()) {
                    searchViewModel.showHistoryList()
                } else {
                    searchViewModel.hideHistoryList()
                }

                searchViewModel.searchDebounce(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        binding.searchEditText.addTextChangedListener(simpleTextWatcher)

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTrack()
            }
            false
        }

        binding.technicalErrorButton.setOnClickListener {
            searchTrack()
        }

        binding.clearHistoryButton.setOnClickListener {
            searchViewModel.clearHistory()
            searchViewModel.hideHistoryList()
        }


        binding.searchEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && binding.searchEditText.text.isEmpty()) {
                searchViewModel.showHistoryList()
            } else {
                searchViewModel.hideHistoryList()
            }
        }

//        binding.searchEditText.setText(searchViewModel.getLastQuery())

    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun showTrackHistory() {
        if (adapterHistory.itemCount > 0) {
            binding.clearHistoryButton.visibility = View.VISIBLE
            binding.searchHistoryText.visibility = View.VISIBLE
            binding.historyList.visibility = View.VISIBLE
        }
        binding.trackList.visibility = View.GONE
    }

    private fun hideTrackHistory() {
        binding.clearHistoryButton.visibility = View.GONE
        binding.searchHistoryText.visibility = View.GONE
        binding.historyList.visibility = View.GONE
        binding.trackList.visibility = View.VISIBLE
    }

    fun onTrackClick(trackId: Int) {
        if (clickDebounce()) {
            searchViewModel.clickTrack(requireContext(), trackId)
        }
    }
}