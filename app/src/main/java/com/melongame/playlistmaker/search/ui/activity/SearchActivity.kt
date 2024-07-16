package com.melongame.playlistmaker.search.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.melongame.playlistmaker.R
import com.melongame.playlistmaker.player.ui.MediaPlayerActivity
import com.melongame.playlistmaker.search.domain.models.Track
import com.melongame.playlistmaker.search.ui.view_model.SearchState
import com.melongame.playlistmaker.search.ui.view_model.SearchViewModel


class SearchActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchViewModel
    private var inputText: String = TEXT_DEFAULT
    private val adapter = TrackAdapter()

    private lateinit var updateButton: Button
    private lateinit var connectTrouble: FrameLayout
    private lateinit var searchNothing: FrameLayout
    private lateinit var inputEditText: EditText
    private lateinit var searchTracks: RecyclerView
    private lateinit var lastTrack: String
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var searchHistoryLayout: LinearLayout
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val backView = findViewById<ImageView>(R.id.back_light)
        val clearButton = findViewById<LinearLayout>(R.id.clearIcon)
        val clearHistory = findViewById<Button>(R.id.clear_history_button)
        val historyRecyclerView = findViewById<RecyclerView>(R.id.history_recycler_view)

        viewModel = ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory()
        )[SearchViewModel::class.java]
        viewModel.observeState().observe(this) {
            setState(it)
        }

        historyAdapter = TrackAdapter()
        historyRecyclerView.adapter = historyAdapter
        historyRecyclerView.layoutManager = LinearLayoutManager(this)

        searchHistoryLayout = findViewById(R.id.search_history_linear_layout)
        searchTracks = findViewById(R.id.track_search)
        updateButton = findViewById(R.id.search_update_button)
        connectTrouble = findViewById(R.id.connect_trouble)
        searchNothing = findViewById(R.id.search_nothing)
        inputEditText = findViewById(R.id.searchEditText)
        progressBar = findViewById(R.id.progressBar)

        adapter.onItemClick = { track -> onTrackClicked(track) }

        historyAdapter.onItemClick = { track -> onTrackClicked(track) }

        searchTracks.adapter = adapter
        searchTracks.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        historyVisibility()

        backView.setOnClickListener { finish() }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            val currentFocus = currentFocus
            if (currentFocus != null) {
                inputMethodManager?.hideSoftInputFromWindow(currentFocus.windowToken, 0)
                viewModel.clearTracks()
                adapter.notifyDataSetChanged()
                Log.d("SearchChanger", "Удален текст из поля поиска!")
            }
        }

        updateButton.setOnClickListener { viewModel.retryLastSearch() }


        clearHistory.setOnClickListener {
            viewModel.clearSearchHistory()
            historyAdapter.notifyDataSetChanged()
            searchHistoryLayout.isVisible = false
            Log.d("SearchChanger", "История поиска отчищена!")
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (inputEditText.text.isNotEmpty()) {
                    searchRequest(inputEditText.text.toString())
                }
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                val currentFocus = currentFocus
                if (currentFocus != null) {
                    inputMethodManager?.hideSoftInputFromWindow(currentFocus.windowToken, 0)
                }
                true
            } else false
        }


        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            searchHistoryLayout.isVisible =
                hasFocus && inputEditText.text.isEmpty() && viewModel.getSearchHistory()
                    .isNotEmpty()
        }


        inputEditText.addTextChangedListener(
            beforeTextChanged = { _, _, _, _ -> },
            onTextChanged = { charSequence, _, _, _ ->
                hideAllViews()
                clearButton.isVisible = !charSequence.isNullOrEmpty()
                searchHistoryLayout.isVisible =
                    charSequence.isNullOrEmpty() && inputEditText.hasFocus() && viewModel.getSearchHistory()
                        .isNotEmpty()
                viewModel.onSearchTextChanged(charSequence.toString())
            },
            afterTextChanged = { _ ->
                inputText = inputEditText.text.toString()
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SEARCH_TEXT, viewModel.searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        viewModel.searchText = savedInstanceState.getString(KEY_SEARCH_TEXT, TEXT_DEFAULT)
    }

    private fun hideAllViews() {
        searchTracks.isVisible = false
        progressBar.isVisible = false
        connectTrouble.isVisible = false
        updateButton.isVisible = false
        searchNothing.isVisible = false
        searchHistoryLayout.isVisible = false
    }

    private fun showProgressbar() {
        hideAllViews()
        progressBar.isVisible = true
    }

    private fun showTrackList() {
        hideAllViews()
        searchTracks.isVisible = true
    }

    private fun showServerError() {
        viewModel.clearTracks()
        adapter.notifyDataSetChanged()
        hideAllViews()
        connectTrouble.isVisible = true
        updateButton.isVisible = true
    }

    private fun showNotFound() {
        hideAllViews()
        searchNothing.isVisible = true
    }


    private fun searchRequest(track: String) {
        lastTrack = track
        viewModel.searchRequest(track)
    }

    private fun historyVisibility() {
        val history = viewModel.getSearchHistory()
        if (history.isNotEmpty() && inputEditText.hasFocus()) {
            historyAdapter.tracks = history as ArrayList<Track>
            historyAdapter.notifyDataSetChanged()
            searchHistoryLayout.isVisible = true
        } else {
            if (history.isNotEmpty()) {
                historyAdapter.tracks = history as ArrayList<Track>
                historyAdapter.notifyDataSetChanged()
            }
            searchHistoryLayout.isVisible = false
        }
    }

    private fun onTrackClicked(track: Track) {

        if (viewModel.clickDebounce()) {
            viewModel.saveTrack(track)
            historyAdapter.tracks = viewModel.getSearchHistory() as ArrayList<Track>
            historyAdapter.notifyDataSetChanged()

            val audioPlayerIntent = Intent(this, MediaPlayerActivity::class.java)
            audioPlayerIntent.putExtra(KEY_TRACK_JSON, Gson().toJson(track))
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
        const val KEY_TRACK_JSON = "trackJson"
        const val KEY_SEARCH_TEXT = "KEY_SEARCH_TEXT"
        const val TEXT_DEFAULT = ""
    }
}