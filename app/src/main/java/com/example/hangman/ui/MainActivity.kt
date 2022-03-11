package com.example.hangman.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.activity.viewModels
import com.example.hangman.R

const val WORDNIK_API_KEY = "4zk263i5zxo3f2awyz8g2ms9b3oo5rdd15m5tv7fbilpdcguk"

class MainActivity : AppCompatActivity() {
    private val viewModel : WordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

//        val toolbar: Toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)

        //Buttons
        val button_Play = findViewById<Button>(R.id.button_play)
        val button_Settings = findViewById<Button>(R.id.button_settings)
        val button_wotd = findViewById<Button>(R.id.button_wotd)

        //Button listeners
        button_Play?.setOnClickListener(){
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
        button_Settings?.setOnClickListener(){
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        button_wotd?.setOnClickListener(){
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
    }
}