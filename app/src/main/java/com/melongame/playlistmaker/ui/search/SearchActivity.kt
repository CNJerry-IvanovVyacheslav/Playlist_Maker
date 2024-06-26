package com.melongame.playlistmaker.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.melongame.playlistmaker.R
import com.melongame.playlistmaker.data.network.ITunesApiService
import com.melongame.playlistmaker.presentation.SearchHistoryControl
import com.melongame.playlistmaker.presentation.TrackAdapter
import com.melongame.playlistmaker.data.dto.TracksResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private var str: CharSequence? = null
    private var textOfSearch: String = ""
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchTracks(inputEditText.text.toString()) }
    private lateinit var inputEditText: EditText
    private lateinit var searchTracks: RecyclerView
    private lateinit var searchNothing: FrameLayout
    private lateinit var connectTrouble: FrameLayout
    private lateinit var searchHistoryLinearLayout: LinearLayout
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var searchHistoryControl: SearchHistoryControl
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchHistoryControl = SearchHistoryControl(this)
        searchHistoryLinearLayout = findViewById(R.id.search_history_linear_layout)
        progressBar = findViewById(R.id.progressBar)

        initHistoryRecycler()


        inputEditText = findViewById(R.id.searchEditText)
        searchNothing = findViewById(R.id.search_nothing)
        connectTrouble = findViewById(R.id.connect_trouble)
        searchTracks = findViewById(R.id.track_search)


        val updateButton = findViewById<Button>(R.id.search_update_button)
        val backButton = findViewById<ImageView>(R.id.back_light)
        val buttonClear = findViewById<LinearLayout>(R.id.clearIcon)
        val clearHistoryButton = findViewById<Button>(R.id.clear_history_button)

        fun hideKeyboard() {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val textOfSearch = inputEditText.text.toString()
                searchTracks(textOfSearch)
                hideKeyboard()
                true
            } else {
                false
            }
        }

        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && textOfSearch.isEmpty()) {
                searchHistoryLinearLayout.isVisible =
                    searchHistoryControl.getSearchHistory().isNotEmpty()
            } else {
                searchHistoryLinearLayout.isVisible = false
            }
        }

        clearHistoryButton.setOnClickListener {
            searchHistoryControl.clearSearchHistory()
            searchHistoryLinearLayout.isVisible = false
        }

        backButton.setOnClickListener {
            finish()
        }

        updateButton.setOnClickListener {
            searchTracks(textOfSearch)
        }

        buttonClear.setOnClickListener {
            inputEditText.setText("")
            inputEditText.clearFocus()
            searchDebounce(0)
            hideKeyboard()
        }


        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                str = s
                buttonClear.isVisible = !s.isNullOrEmpty()
                searchDebounce(SEARCH_DEBOUNCE_DELAY)
                if (textOfSearch.isEmpty()) {
                    searchHistoryLinearLayout.isVisible =
                        searchHistoryControl.getSearchHistory().isNotEmpty()
                } else {
                    searchHistoryLinearLayout.isVisible = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_NAME, str.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        str = savedInstanceState.getString(SEARCH_NAME, null)
        inputEditText.setText(str)
    }

    private fun searchDebounce(mils: Long) {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, mils)
    }

    private fun initHistoryRecycler() {
        historyRecyclerView = findViewById(R.id.history_recycler_view)
        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        val searchHistory = searchHistoryControl.getSearchHistory().toList()
        val historyAdapter = TrackAdapter(this, searchHistory, searchHistoryControl)
        historyRecyclerView.adapter = historyAdapter
        searchHistoryControl.historyAdapter = historyAdapter
    }

    private fun searchTracks(textOfSearch: String) {
        progressBar.isVisible = true
        searchHistoryLinearLayout.isVisible = false
        if (textOfSearch.isEmpty()) {
            searchTracks.isVisible = false
            searchNothing.isVisible = false
            connectTrouble.isVisible = false
            progressBar.isVisible = false
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val iTunesApiService = retrofit.create(ITunesApiService::class.java)

        if (textOfSearch.isNotEmpty()) {
            iTunesApiService.search(textOfSearch).enqueue(object : Callback<TracksResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>
                ) {
                    progressBar.isVisible = false
                    if (response.isSuccessful) {
                        val trackResult = response.body()
                        if (trackResult != null) {
                            val tracks = trackResult.results.toList()
                            if (tracks.isNotEmpty()) {
                                val adapter =
                                    TrackAdapter(this@SearchActivity, tracks, searchHistoryControl)
                                searchTracks.adapter = adapter
                                searchTracks.isVisible = true
                                searchNothing.isVisible = false
                                connectTrouble.isVisible = false
                            } else {
                                searchTracks.isVisible = false
                                searchNothing.isVisible = true
                                connectTrouble.isVisible = false
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    searchTracks.isVisible = false
                    searchNothing.isVisible = false
                    connectTrouble.isVisible = true
                    progressBar.isVisible = false
                }
            })
        }
    }

    private companion object {
        const val SEARCH_NAME = "NAME"
        const val BASE_URL = "https://itunes.apple.com"
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}