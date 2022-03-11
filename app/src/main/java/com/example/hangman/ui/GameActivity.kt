package com.example.hangman.ui

import android.app.SearchManager
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.hangman.R
import com.example.hangman.data.LoadingStatus
import com.google.android.material.progressindicator.CircularProgressIndicator
import org.w3c.dom.Text


class GameActivity: AppCompatActivity() {

    //Loading and error variables
    private lateinit var searchErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator

    //Viewmodel
    private val viewModel : WordViewModel by viewModels()

    //API and display strings
    private lateinit var apiWord : String //will need to be lateinit when set to the api response
    private lateinit var display : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        //Fade when the user clicks "PLAY AGAIN"
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        //Buttons
        var button_play_again = findViewById<Button>(R.id.btn_play_again)
        var button_define = findViewById<Button>(R.id.btn_define)
        var button_menu = findViewById<Button>(R.id.btn_menu)

        //Textview of the display word
        val displayTV : TextView = findViewById(R.id.display_word)

        //Set Loading & Error variables
        searchErrorTV = findViewById(R.id.tv_search_error)
        loadingIndicator = findViewById(R.id.loading_indicator)

        
        //Preference stuff
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        val gameTries = sharedPrefs.getInt(
            getString(R.string.pref_tries_key),
            6
        )
        val wordLength = sharedPrefs.getInt(
            getString(R.string.pref_letter_count_key),
            5
        )

        //Api stuff
        viewModel.loadReqResult(wordLength, WORDNIK_API_KEY)
        viewModel.wordResult.observe(this) { word ->
            if(word != null){
                Log.d("tag", word)
                apiWord = word
                display = toUnderscore(word)
                Log.d("tag-display", display)
                displayTV.text = display
            }
        }

        viewModel.loadingStatus.observe(this) { uiState ->
            when (uiState) {
                LoadingStatus.LOADING -> {
                    button_play_again.visibility = View.INVISIBLE
                    button_define.visibility = View.INVISIBLE
                    button_menu.visibility = View.INVISIBLE
                    loadingIndicator.visibility = View.VISIBLE
                    searchErrorTV.visibility = View.INVISIBLE
                }
                LoadingStatus.ERROR -> {
                    button_play_again.visibility = View.VISIBLE
                    button_define.visibility = View.VISIBLE
                    button_menu.visibility = View.VISIBLE
                    loadingIndicator.visibility = View.INVISIBLE
                    searchErrorTV.visibility = View.VISIBLE
                }
                else -> {
                    button_play_again.visibility = View.VISIBLE
                    button_define.visibility = View.VISIBLE
                    button_menu.visibility = View.VISIBLE
                    loadingIndicator.visibility = View.INVISIBLE
                    searchErrorTV.visibility = View.INVISIBLE
                }
            }
        }

        //Button listeners
        button_play_again?.setOnClickListener(){
            val intent = Intent(this, GameActivity::class.java)
            finish()
            startActivity(intent)
        }
        button_define?.setOnClickListener(){
            val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
                putExtra(
                    SearchManager.QUERY, getString(
                    R.string.definition,
                    apiWord
                ))
            }
            try{
                startActivity(intent)
            }catch(e: ActivityNotFoundException) {
                Toast.makeText(this@GameActivity, getString(R.string.search_error), Toast.LENGTH_SHORT).show()
            }
        }
        button_menu?.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent)
        }
        button_define?.setOnClickListener(){
            val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
                putExtra(
                    SearchManager.QUERY, getString(
                    R.string.definition,
                    apiWord
                ))
            }
            try{
                startActivity(intent)
            }catch(e: ActivityNotFoundException) {
                Toast.makeText(this@GameActivity, getString(R.string.search_error), Toast.LENGTH_SHORT).show()
            }

        }
        button_menu?.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    private fun toUnderscore(original : String) : String {
        val length = original.length
        var temp : String = ""

        for (i in 1..length) {
            temp += "_"
        }

        return temp
    }

}