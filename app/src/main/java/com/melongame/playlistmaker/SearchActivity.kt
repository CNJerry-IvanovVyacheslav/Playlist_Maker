package com.melongame.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible

class SearchActivity : AppCompatActivity() {

    private var str: CharSequence? = null
    private var backButton: ImageView? = null
    private var buttonClear: LinearLayout? = null
    private var editText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        backButton = findViewById(R.id.back_light)
        buttonClear = findViewById(R.id.clearIcon)
        editText = findViewById(R.id.searchEditText)

        backButton?.setOnClickListener {
            finish()
        }
        buttonClear?.setOnClickListener {
            editText?.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(editText?.windowToken, 0)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                buttonClear?.isVisible = true
                buttonClear?.isVisible = false
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        editText?.addTextChangedListener(simpleTextWatcher)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_NAME, str.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        str = savedInstanceState.getString(SEARCH_NAME, null)
        editText?.setText(str)
    }

    companion object {
        private const val SEARCH_NAME = "NAME"
    }

}