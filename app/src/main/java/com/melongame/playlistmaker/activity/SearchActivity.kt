package com.melongame.playlistmaker.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.melongame.playlistmaker.R
import com.melongame.playlistmaker.databinding.TrackListBinding
import com.melongame.playlistmaker.mock.ListTrack
import com.melongame.playlistmaker.tracks.TrackAdapter

class SearchActivity : AppCompatActivity() {

    private var str: CharSequence? = null
    private lateinit var inputEditText: EditText
    private lateinit var searchTracks: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backButton = findViewById<ImageView>(R.id.back_light)
        val buttonClear = findViewById<LinearLayout>(R.id.clearIcon)
        inputEditText = findViewById(R.id.searchEditText)

        backButton.setOnClickListener {
            finish()
        }
        buttonClear.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                str = s
                buttonClear.isVisible = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        searchTracks = findViewById(R.id.track_search)

        searchTracks.adapter = TrackAdapter(ListTrack.tracks)

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

    private companion object {
        const val SEARCH_NAME = "NAME"
    }
}