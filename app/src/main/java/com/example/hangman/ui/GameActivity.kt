package com.example.hangman.ui

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.hangman.R
import com.example.hangman.data.LoadingStatus
import com.google.android.material.progressindicator.CircularProgressIndicator




class GameActivity: AppCompatActivity() {
    private lateinit var searchErrorTV: TextView

    private lateinit var loadingIndicator: CircularProgressIndicator
    private val viewModel : WordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val newGameButton: Button = findViewById(R.id.new_game_button)

        searchErrorTV = findViewById(R.id.tv_search_error)
        loadingIndicator = findViewById(R.id.loading_indicator)

        //preference stuff
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        val gameTries = sharedPrefs.getInt(
            getString(R.string.pref_tries_key),
            6
        )
        val wordLength = sharedPrefs.getInt(
            getString(R.string.pref_letter_count_key),
            5
        )

        //api stuff
        viewModel.loadReqResult(wordLength, WORDNIK_API_KEY)
        viewModel.wordResult.observe(this) { word ->
            if(word != null){
                Log.d("tag", word)
            }
        }

        viewModel.loadingStatus.observe(this) { uiState ->
            when (uiState) {
                LoadingStatus.LOADING -> {
                    newGameButton.visibility = View.INVISIBLE
                    loadingIndicator.visibility = View.VISIBLE
                    searchErrorTV.visibility = View.INVISIBLE
                }
                LoadingStatus.ERROR -> {
                    newGameButton.visibility = View.VISIBLE
                    loadingIndicator.visibility = View.INVISIBLE
                    searchErrorTV.visibility = View.VISIBLE
                }
                else -> {
                    newGameButton.visibility = View.VISIBLE
                    loadingIndicator.visibility = View.INVISIBLE
                    searchErrorTV.visibility = View.INVISIBLE
                }
            }
        }

        newGameButton.setOnClickListener{
            val intent = Intent(this, GameActivity::class.java)
            finish()
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }


}