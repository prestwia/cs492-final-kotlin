package com.example.hangman.ui

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.hangman.R

class GameActivity: AppCompatActivity() {

    private val viewModel : WordViewModel by viewModels()
//    private lateinit var theWord: String
//    private var theWord = findViewById<TextView>(R.id.the_word)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)


        viewModel.loadReqResult(7, WORDNIK_API_KEY)
        viewModel.wordResult.observe(this) { word ->
            if(word != null){
                Log.d("GameActivity", word)
//                theWord.text = word

            }
        }

        val newGameButton: Button = findViewById(R.id.new_game_button)
//        val actualWordValue




        newGameButton.setOnClickListener{
            val intent = Intent(this, GameActivity::class.java)
            finish()
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }


}