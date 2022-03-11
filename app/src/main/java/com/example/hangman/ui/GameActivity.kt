package com.example.hangman.ui

import android.app.SearchManager
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.hangman.R
import com.example.hangman.data.LoadingStatus
import com.google.android.material.progressindicator.CircularProgressIndicator

class GameActivity: AppCompatActivity() {

    //Layout attributes
    private lateinit var guessET : EditText
    private lateinit var guessBtn : Button
    private lateinit var displayTV : TextView
    private lateinit var displayIncorrectTV : TextView

    //Loading and error variables
    private lateinit var apiErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator

    //Viewmodel
    private val viewModel : WordViewModel by viewModels()

    //API and display strings
    private lateinit var answer : String
    private lateinit var display : String
    private var incorrectGuesses = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        //Fade when the user clicks "PLAY AGAIN"
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        //Buttons
        val buttonPlayAgain = findViewById<Button>(R.id.btn_play_again)
        val buttonDefine = findViewById<Button>(R.id.btn_define)
        val buttonMenu = findViewById<Button>(R.id.btn_menu)

        displayTV = findViewById(R.id.display_word)
        guessBtn = findViewById(R.id.guess_button)
        guessET = findViewById(R.id.guess_text)
        displayIncorrectTV = findViewById(R.id.incorrect_guesses)

        //Textview of the display word
        val displayTV : TextView = findViewById(R.id.display_word)

        //Set Loading & Error variables
        apiErrorTV = findViewById(R.id.tv_api_error)
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
                
                answer = word

                display = toUnderscore(word)
                Log.d("tag-display", display)
                displayTV.text = display
            }
        }

        //Loading + error indicators
        viewModel.loadingStatus.observe(this) { uiState ->
            when (uiState) {
                LoadingStatus.LOADING -> {
                    buttonPlayAgain.visibility = View.INVISIBLE
                    buttonDefine.visibility = View.INVISIBLE
                    buttonMenu.visibility = View.INVISIBLE
                    loadingIndicator.visibility = View.VISIBLE
                    apiErrorTV.visibility = View.INVISIBLE
                }
                LoadingStatus.ERROR -> {
                    buttonPlayAgain.visibility = View.VISIBLE
                    buttonDefine.visibility = View.VISIBLE
                    buttonMenu.visibility = View.VISIBLE
                    loadingIndicator.visibility = View.INVISIBLE
                    apiErrorTV.visibility = View.VISIBLE
                }
                else -> {
                    buttonPlayAgain.visibility = View.VISIBLE
                    buttonDefine.visibility = View.VISIBLE
                    buttonMenu.visibility = View.VISIBLE
                    loadingIndicator.visibility = View.INVISIBLE
                    apiErrorTV.visibility = View.INVISIBLE
                }
            }
        }

        //Button listeners
        buttonPlayAgain?.setOnClickListener(){
            val intent = Intent(this, GameActivity::class.java)
            finish()
            startActivity(intent)

        }
        buttonDefine?.setOnClickListener(){
            val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
                putExtra(
                    SearchManager.QUERY, getString(
                    R.string.definition,
                    answer
                ))
            }
            try{
                startActivity(intent)
            }catch(e: ActivityNotFoundException) {
                Toast.makeText(this@GameActivity, getString(R.string.search_error), Toast.LENGTH_SHORT).show()
            }

        }
        buttonMenu?.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        guessBtn.setOnClickListener{
            val guess = guessET.text.toString()
            if (guess != "") {
                makeGuess(guess)
                guessET.getText().clear();
            }
            else {
                Toast.makeText(this, "Enter a guess (Letter)", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun makeGuess(guess : String) {
        Log.d("tag-guess", "Guess: $guess")
        if (answer.contains(guess)) {
            val index = mutableListOf<Int>()
            /* get indexes of all chars that are the same as guess */
            for (i in answer.indices) {
                if (answer[i].toString() == guess) {
                    index.add(i)
                }
            }

            for (i in index) {
                Log.d("tag-guess", "Index: $i")
            }

            var new_display = display
            /* construct new display string */
            for (i in index) {
                 new_display= new_display.substring(0, i) + guess + new_display.substring(i + 1)
            }
            display = new_display
            displayTV.text = display
            Log.d("tag-guess", "New String: $new_display")
        }
        else {
            incorrectGuesses += guess
            displayIncorrectTV.text = incorrectGuesses
            Log.d("tag-guess", "Incorrect: $incorrectGuesses")
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