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
    private lateinit var buttonPlayAgain : Button
    private lateinit var buttonDefine : Button
    private lateinit var buttonMenu : Button
    private lateinit var guessPrompt: TextView
    private lateinit var incorrectGuess: TextView

    //Loading and error variables
    private lateinit var apiErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator

    //Viewmodel
    private val viewModel : WordViewModel by viewModels()

    //API and display strings
    private lateinit var answer : String
    private lateinit var display : String
    private var incorrectGuesses = ""

    //Game Status?
    private lateinit var gameResult: String

    // user globals from settings
    private var gameTries: Int = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        //Fade when the user clicks "PLAY AGAIN"
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        //Buttons
        buttonPlayAgain = findViewById<Button>(R.id.btn_play_again)
        buttonDefine = findViewById<Button>(R.id.btn_define)
        buttonMenu = findViewById<Button>(R.id.btn_menu)

        displayTV = findViewById(R.id.display_word)
        guessBtn = findViewById(R.id.guess_button)
        guessET = findViewById(R.id.guess_text)
        displayIncorrectTV = findViewById(R.id.incorrect_guesses)

        //Textview of the display word
        val displayTV : TextView = findViewById(R.id.display_word)

        //Set Loading & Error variables
        apiErrorTV = findViewById(R.id.tv_api_error)
        loadingIndicator = findViewById(R.id.loading_indicator)

        //prompts
        guessPrompt= findViewById(R.id.incorrect_guess_prompt)
        incorrectGuess= findViewById(R.id.guess_prompt)
        //Preference stuff
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        gameTries = sharedPrefs.getInt(
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
        // set gameResult to int so that it can hide the end game buttons.
//        gameResult=1

        //Loading + error indicators
        viewModel.loadingStatus.observe(this) { uiState ->
            when (uiState) {
                LoadingStatus.LOADING -> {
                    buttonPlayAgain.visibility = View.INVISIBLE
                    buttonDefine.visibility = View.INVISIBLE
                    buttonMenu.visibility = View.INVISIBLE
                    loadingIndicator.visibility = View.VISIBLE
                    apiErrorTV.visibility = View.INVISIBLE
                    displayTV.visibility = View.INVISIBLE
                    guessBtn.visibility = View.INVISIBLE
                    guessET.visibility = View.INVISIBLE
                }
                LoadingStatus.ERROR -> {
                    buttonPlayAgain.visibility = View.VISIBLE
                    buttonDefine.visibility = View.VISIBLE
                    buttonMenu.visibility = View.VISIBLE
                    loadingIndicator.visibility = View.INVISIBLE
                    apiErrorTV.visibility = View.VISIBLE
                    displayTV.visibility = View.INVISIBLE
                    guessBtn.visibility = View.INVISIBLE
                    guessET.visibility = View.INVISIBLE
                }
//                LoadingStatus.SUCCESS->{
//                    buttonPlayAgain.visibility = View.INVISIBLE
//                    buttonDefine.visibility = View.INVISIBLE
//                    buttonMenu.visibility = View.INVISIBLE
//                    loadingIndicator.visibility = View.INVISIBLE
//                    apiErrorTV.visibility = View.INVISIBLE
//                }

                else -> {
//                    Log.d("tag-guess", "inside else statement for loading status")
                    //testing here for a minute
//                    when(gameResult){
//                        is String -> {
//                            buttonPlayAgain.visibility = View.VISIBLE
//                            buttonDefine.visibility = View.INVISIBLE
//                            buttonMenu.visibility = View.INVISIBLE
//                            loadingIndicator.visibility = View.INVISIBLE
//                            apiErrorTV.visibility = View.INVISIBLE}
//
//                    }

                    buttonPlayAgain.visibility = View.INVISIBLE
                    buttonDefine.visibility = View.INVISIBLE
                    buttonMenu.visibility = View.INVISIBLE
                    loadingIndicator.visibility = View.INVISIBLE
                    apiErrorTV.visibility = View.INVISIBLE
                    displayTV.visibility = View.VISIBLE
                    guessBtn.visibility = View.VISIBLE
                    guessET.visibility = View.VISIBLE
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
                // tries = 10
                gameWinner(gameTries,answer, display);
                guessET.getText().clear();
            }
            else {
                Toast.makeText(this, "Enter a guess (Letter)", Toast.LENGTH_SHORT).show()
            }
        }

    } // end onCreate

    // add game status function before make guess to generate a end of game status that turns
    // the end game buttons from invisible to visible

    //    private fun gameStatusButtons(win: String){
//        if (win== Winner){
//              visibility of the 3 buttons here.
//        }
//        else{
//                  user has lost
//        }
//    }
                            // user settings global
    private fun gameWinner(tries: Int, word: String, curGuess:String){
//        1 user runs out of tries -- user loses
//         user wins
//          and Tries!=0
        if (word==curGuess)
        {
//            Log.d("gameWin", "user wins")
            displayTV.visibility = View.VISIBLE
            guessBtn.visibility = View.INVISIBLE
            guessET.visibility = View.INVISIBLE
            guessPrompt.visibility= View.INVISIBLE
            incorrectGuess.visibility= View.INVISIBLE

            buttonPlayAgain.visibility = View.VISIBLE
            buttonDefine.visibility = View.VISIBLE
            buttonMenu.visibility = View.VISIBLE
        } else {
            if (tries==0){

//                Log.d("testgamewiner", "in the else")
                displayTV.visibility = View.VISIBLE
                guessBtn.visibility = View.INVISIBLE
                guessET.visibility = View.INVISIBLE
                guessPrompt.visibility= View.INVISIBLE
                incorrectGuess.visibility= View.INVISIBLE
                buttonPlayAgain.visibility = View.VISIBLE
                buttonDefine.visibility = View.VISIBLE
                buttonMenu.visibility = View.VISIBLE
            }
        }
    }

    private fun makeGuess(guess : String) {
        Log.d("tag-guess", "Guess: $guess")
        Log.d("numSettings", "tries: $gameTries")

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
            // SET game result to string so it displays the end game buttons.
//            if tries is less than total tries OR display==wordFromAPI
//                    User Won
//            else user Lost
//            gameResult="WINNER"
        }
        else {
            incorrectGuesses += guess
            displayIncorrectTV.text = incorrectGuesses
            Log.d("tag-guess", "Incorrect: $incorrectGuesses")
            gameTries-=1

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