package com.example.hangman.ui

import android.app.SearchManager
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.hangman.ui.MainActivity
import com.example.hangman.R

class EndScreenActivity : AppCompatActivity() {

    //change this to the word from the api
    val apiWord = "taco"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_screen)

        val button_play_again = findViewById<Button>(R.id.btn_play_again)
        val button_define = findViewById<Button>(R.id.btn_define)
        val button_menu = findViewById<Button>(R.id.btn_menu)
        button_play_again?.setOnClickListener(){
            Toast.makeText(this@EndScreenActivity, R.string.btn_play_again, Toast.LENGTH_SHORT).show()
        }
        button_define?.setOnClickListener(){
//            Toast.makeText(this@EndScreenActivity, R.string.btn_define, Toast.LENGTH_SHORT).show()
            val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
                putExtra(SearchManager.QUERY, getString(
                    R.string.definition,
                    apiWord
                ))
            }
            try{
                startActivity(intent)
            }catch(e: ActivityNotFoundException) {
                Toast.makeText(this@EndScreenActivity, getString(R.string.search_error), Toast.LENGTH_SHORT).show()
            }

        }
        button_menu?.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}