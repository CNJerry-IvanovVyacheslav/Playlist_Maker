package com.melongame.playlistmaker.search.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.melongame.playlistmaker.databinding.FragmentSearchBinding
import com.melongame.playlistmaker.player.ui.activity.MediaPlayerActivity
import com.melongame.playlistmaker.search.domain.models.Track
import com.melongame.playlistmaker.search.ui.view_model.SearchState
import com.melongame.playlistmaker.search.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SearchViewModel>()
    private var inputText: String = TEXT_DEF
    private val adapter = TrackAdapter()
    private lateinit var lastTrack: String
    private lateinit var historyAdapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeState().observe(viewLifecycleOwner) {
            setState(it)
        }

        historyAdapter = TrackAdapter()
        binding.historyRecyclerView.adapter = historyAdapter
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter.onItemClick = { track -> onTrackClicked(track) }

        historyAdapter.onItemClick = { track -> onTrackClicked(track) }

        binding.trackSearch.adapter = adapter
        binding.trackSearch.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        historyVisibility()

        binding.clearIcon.setOnClickListener {
            binding.searchEditText.setText("")
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            val currentFocus = requireActivity().currentFocus
            if (currentFocus != null) {
                inputMethodManager?.hideSoftInputFromWindow(currentFocus.windowToken, 0)
                viewModel.clearTracks()
                adapter.notifyDataSetChanged()
            }
        }

        binding.searchUpdateButton.setOnClickListener { viewModel.retryLastSearch() }


        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearSearchHistory()
            historyAdapter.notifyDataSetChanged()
            binding.searchHistoryTextView.isVisible = false
        }

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.searchEditText.text.isNotEmpty()) {
                    searchRequest(binding.searchEditText.text.toString())
                }
                val inputMethodManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                val currentFocus = requireActivity().currentFocus
                if (currentFocus != null) {
                    inputMethodManager?.hideSoftInputFromWindow(currentFocus.windowToken, 0)
                }
                true
            } else false
        }
        binding.searchEditText.setOnFocusChangeListener { _, hasFocus ->
            binding.searchHistoryLinearLayout.isVisible =
                hasFocus && binding.searchEditText.text.isEmpty() && viewModel.getSearchHistory()
                    .isNotEmpty()
        }


        binding.searchEditText.addTextChangedListener(
            beforeTextChanged = { _, _, _, _ -> },
            onTextChanged = { charSequence, _, _, _ ->
                hideAllViews()
                binding.clearIcon.isVisible = !charSequence.isNullOrEmpty()
                binding.searchHistoryLinearLayout.isVisible =
                    charSequence.isNullOrEmpty() && binding.searchEditText.hasFocus() && viewModel.getSearchHistory()
                        .isNotEmpty()
                viewModel.onSearchTextChanged(charSequence.toString())
            },
            afterTextChanged = { _ ->
                inputText = binding.searchEditText.text.toString()
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SEARCH_TEXT, inputText)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            inputText = savedInstanceState.getString(KEY_SEARCH_TEXT, TEXT_DEF)
        }
    }

    private fun hideAllViews() {
        binding.trackSearch.isVisible = false
        binding.progressBar.isVisible = false
        binding.connectTrouble.isVisible = false
        binding.searchUpdateButton.isVisible = false
        binding.searchNothing.isVisible = false
        binding.searchHistoryLinearLayout.isVisible = false
    }

    private fun showProgressbar() {
        hideAllViews()
        binding.progressBar.isVisible = true
    }

    private fun showTrackList() {
        hideAllViews()
        binding.trackSearch.isVisible = true
    }

    private fun showServerError() {
        viewModel.clearTracks()
        adapter.notifyDataSetChanged()
        hideAllViews()
        binding.connectTrouble.isVisible = true
        binding.searchUpdateButton.isVisible = true
    }

    private fun showNotFound() {
        hideAllViews()
        binding.searchNothing.isVisible = true
    }

    private fun searchRequest(track: String) {
        lastTrack = track
        viewModel.searchRequest(track)
    }

    private fun historyVisibility() {
        val history = viewModel.getSearchHistory()
        if (history.isNotEmpty() && binding.searchEditText.hasFocus()) {
            historyAdapter.tracks = history as ArrayList<Track>
            historyAdapter.notifyDataSetChanged()
            binding.searchHistoryLinearLayout.isVisible = true
        } else {
            if (history.isNotEmpty()) {
                historyAdapter.tracks = history as ArrayList<Track>
                historyAdapter.notifyDataSetChanged()
            }
            binding.searchHistoryLinearLayout.isVisible = false
        }
    }

    private fun onTrackClicked(track: Track) {
        if (viewModel.clickDebounce()) {
            viewModel.saveTrack(track)
            historyAdapter.tracks = viewModel.getSearchHistory() as ArrayList<Track>
            historyAdapter.notifyDataSetChanged()

            val audioPlayerIntent = Intent(requireContext(), MediaPlayerActivity::class.java)
            audioPlayerIntent.putExtra(KEY_TRACK, track)
            startActivity(audioPlayerIntent)
        }
    }

    private fun setState(state: SearchState) {
        when (state) {
            is SearchState.Loading -> showProgressbar()
            is SearchState.TrackList -> {
                showTrackList()
                adapter.tracks = state.tracks
                adapter.notifyDataSetChanged()
            }

            is SearchState.HistoryTrackList -> historyVisibility()
            is SearchState.ServerError -> showServerError()
            is SearchState.NotFound -> showNotFound()
        }
    }

    companion object {
        const val KEY_TRACK = "track"
        const val KEY_SEARCH_TEXT = "KEY_SEARCH_TEXT"
        const val TEXT_DEF = ""
    }

}