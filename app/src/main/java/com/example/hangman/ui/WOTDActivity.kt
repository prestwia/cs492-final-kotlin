package com.example.hangman.ui

import android.app.Activity
import android.app.SearchManager
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
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


class WOTDActivity: AppCompatActivity() {

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
    private lateinit var guessesLeft: TextView
    private lateinit var lossMsg: TextView
    private lateinit var winMsg: TextView

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
        buttonPlayAgain = findViewById(R.id.btn_play_again)
        buttonDefine = findViewById(R.id.btn_define)
        buttonMenu = findViewById(R.id.btn_menu)

        displayTV = findViewById(R.id.display_word)
        guessBtn = findViewById(R.id.guess_button)
        guessET = findViewById(R.id.guess_text)
        displayIncorrectTV = findViewById(R.id.incorrect_guesses)
        guessesLeft = findViewById(R.id.guesses_left)

        //Textview of the display word
        val displayTV : TextView = findViewById(R.id.display_word)

        winMsg = findViewById(R.id.win_msg)
        lossMsg = findViewById(R.id.loss_msg)

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
        viewModel.loadWOTDResult(WORDNIK_API_KEY)
        viewModel.wordOfTheDayResult.observe(this) { word ->
            if(word != null){
                answer = word.lowercase()
                Log.d("tag", answer)

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
                    displayTV.visibility = View.INVISIBLE
                    guessBtn.visibility = View.INVISIBLE
                    guessET.visibility = View.INVISIBLE
                    guessPrompt.visibility= View.INVISIBLE
                    incorrectGuess.visibility= View.INVISIBLE
                    guessesLeft.visibility = View.INVISIBLE
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
                    guessPrompt.visibility= View.INVISIBLE
                    incorrectGuess.visibility= View.INVISIBLE
                    guessesLeft.visibility = View.INVISIBLE
                }
                else -> {

                    buttonPlayAgain.visibility = View.INVISIBLE
                    buttonDefine.visibility = View.INVISIBLE
                    buttonMenu.visibility = View.INVISIBLE
                    loadingIndicator.visibility = View.INVISIBLE
                    apiErrorTV.visibility = View.INVISIBLE
                    displayTV.visibility = View.VISIBLE
                    guessBtn.visibility = View.VISIBLE
                    guessET.visibility = View.VISIBLE
                    guessPrompt.visibility= View.VISIBLE
                    incorrectGuess.visibility= View.VISIBLE
                    guessesLeft.visibility = View.VISIBLE

                    guessET.requestFocus()
                    guessET.showKeyboard()

                }
            }
        }

        //initially set textview for guesses left
        guessesLeft.text = getString(R.string.guesses_left, gameTries)

        //Button listeners
        buttonPlayAgain.setOnClickListener(){
            val intent = Intent(this, WOTDActivity::class.java)
            finish()
            startActivity(intent)

        }
        buttonDefine.setOnClickListener(){
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
                Toast.makeText(this@WOTDActivity, getString(R.string.search_error), Toast.LENGTH_SHORT).show()
            }

        }
        buttonMenu.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        guessBtn.setOnClickListener{
            val guess = guessET.text.toString()
            // don't allow empty guess
            if (guess != "") {
                // don't allow user to re-guess letter
                if (incorrectGuesses.contains(guess) || display.contains(guess)) {
                    guessET.getText().clear();
                    Toast.makeText(this, "Letter already guessed. Try Again", Toast.LENGTH_SHORT).show()
                }
                else {
                    makeGuess(guess)
                    // tries = 10
                    gameWinner(gameTries,answer, display);
                    guessET.getText().clear();
                }
            }
            else {
                Toast.makeText(this, "Enter a guess (Letter)", Toast.LENGTH_SHORT).show()
            }
        }

    } // end onCreate

    // add game status function before make guess to generate a end of game status that turns
    // the end game buttons from invisible to visible

    private fun gameWinner(tries: Int, word: String, curGuess:String){
        if (word==curGuess)
        {
//            Log.d("gameWin", "user wins")
            displayTV.text = answer
            displayTV.visibility = View.VISIBLE
            guessBtn.visibility = View.INVISIBLE
            this.hideKeyboard()
            guessET.visibility = View.INVISIBLE
            guessPrompt.visibility= View.INVISIBLE
            incorrectGuess.visibility= View.INVISIBLE
            buttonPlayAgain.visibility = View.VISIBLE
            buttonDefine.visibility = View.VISIBLE
            buttonMenu.visibility = View.VISIBLE
            guessesLeft.visibility = View.INVISIBLE
            displayIncorrectTV.visibility = View.INVISIBLE
            winMsg.visibility = View.VISIBLE
          
        } else {
            if (tries==0){
//                Log.d("testgamewiner", "in the else")
                displayTV.text = answer
                displayTV.visibility = View.VISIBLE
                guessBtn.visibility = View.INVISIBLE
                this.hideKeyboard()
                guessET.visibility = View.INVISIBLE
                guessPrompt.visibility= View.INVISIBLE
                incorrectGuess.visibility= View.INVISIBLE
                buttonPlayAgain.visibility = View.VISIBLE
                buttonDefine.visibility = View.VISIBLE
                buttonMenu.visibility = View.VISIBLE
                guessesLeft.visibility = View.INVISIBLE
                displayIncorrectTV.visibility = View.INVISIBLE
                lossMsg.visibility = View.VISIBLE
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
        }
        else {
            incorrectGuesses += guess
            displayIncorrectTV.text = incorrectGuesses
            Log.d("tag-guess", "Incorrect: $incorrectGuesses")
            gameTries-=1
            guessesLeft.text = getString(R.string.guesses_left, gameTries)
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

    //https://stackoverflow.com/questions/1109022/how-do-you-close-hide-the-android-soft-keyboard-programmatically
    fun Activity.hideKeyboard(): Boolean {
        return (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow((currentFocus ?: View(this)).windowToken, 0)
    }

    fun EditText.showKeyboard(): Boolean {
        return (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
            .showSoftInput(this, 0)
    }
}