package com.melongame.playlistmaker.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.melongame.playlistmaker.R
import com.melongame.playlistmaker.tracks.ITunesApiService
import com.melongame.playlistmaker.tracks.TrackAdapter
import com.melongame.playlistmaker.tracks.TracksResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private var str: CharSequence? = null

    private lateinit var inputEditText: EditText
    private lateinit var searchTracks: RecyclerView
    private lateinit var searchNothing: FrameLayout
    private lateinit var connectTrouble: FrameLayout
    private lateinit var textOfSearch: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        inputEditText = findViewById(R.id.searchEditText)
        searchNothing = findViewById(R.id.search_nothing)
        connectTrouble = findViewById(R.id.connect_trouble)
        searchTracks = findViewById(R.id.track_search)

        val updateButton = findViewById<Button>(R.id.search_update_button)
        val backButton = findViewById<ImageView>(R.id.back_light)
        val buttonClear = findViewById<LinearLayout>(R.id.clearIcon)

        fun hideKeyboard() {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                textOfSearch = inputEditText.text.toString()
                searchTracks(textOfSearch)
                hideKeyboard()
                true
            } else {
                false
            }
        }

        backButton.setOnClickListener {
            finish()
        }

        updateButton.setOnClickListener {
            searchTracks(textOfSearch)
        }

        buttonClear.setOnClickListener {
            inputEditText.setText("")
            hideKeyboard()
        }


        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                str = s
                buttonClear.isVisible = !s.isNullOrEmpty()
                searchTracks(SEARCH_TEXT_DEF)
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

    private fun searchTracks(textOfSearch: String) {
        if (textOfSearch.isEmpty()) {
            searchTracks.isVisible = false
            searchNothing.isVisible = false
            connectTrouble.isVisible = false
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val iTunesApiService = retrofit.create(ITunesApiService::class.java)

        if (textOfSearch.isNotEmpty()) {
            iTunesApiService.search(textOfSearch).enqueue(object : Callback<TracksResponse> {
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>
                ) {
                    if (response.isSuccessful) {
                        val trackResult = response.body()
                        if (trackResult != null) {
                            val tracks = trackResult.results
                            if (tracks.isNotEmpty()) {
                                val adapter = TrackAdapter(tracks)
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
                }
            })
        }
    }

    private companion object {
        const val SEARCH_TEXT_DEF: String = ""
        const val SEARCH_NAME = "NAME"
        const val BASE_URL = "https://itunes.apple.com"
    }
}