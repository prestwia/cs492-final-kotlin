package com.example.hangman.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import com.example.hangman.R

const val WORDNIK_API_KEY = "4zk263i5zxo3f2awyz8g2ms9b3oo5rdd15m5tv7fbilpdcguk"

class MainActivity : AppCompatActivity() {
    private val viewModel : WordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button_Play = findViewById<Button>(R.id.button_play)

        val button_Settings = findViewById<Button>(R.id.button_settings)
        val button_wotd = findViewById<Button>(R.id.button_wotd)

        button_Play?.setOnClickListener(){
//            Toast.makeText(this@MainActivity, R.string.message_Hang, Toast.LENGTH_SHORT).show()
//            val intent = Intent(this, EndScreenActivity::class.java)
//            startActivity(intent)
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)

            viewModel.loadReqResult(7, WORDNIK_API_KEY)
        }

        button_Settings?.setOnClickListener(){
//            Toast.makeText(this@MainActivity, R.string.message_settings, Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)

        }
        button_wotd?.setOnClickListener(){
            Toast.makeText(this@MainActivity, R.string.message_wotd, Toast.LENGTH_SHORT).show()
        }
    }
}