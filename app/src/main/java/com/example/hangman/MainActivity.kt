package com.example.hangman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button_Play = findViewById<Button>(R.id.button_play)
        val button_Settings = findViewById<Button>(R.id.button_settings)
        val button_wotd = findViewById<Button>(R.id.button_wotd)
        button_Play?.setOnClickListener(){
            Toast.makeText(this@MainActivity, R.string.message_Hang, Toast.LENGTH_SHORT).show()
        }
        button_Settings?.setOnClickListener(){
            Toast.makeText(this@MainActivity, R.string.message_settings, Toast.LENGTH_SHORT).show()
        }
        button_wotd?.setOnClickListener(){
            Toast.makeText(this@MainActivity, R.string.message_wotd, Toast.LENGTH_SHORT).show()
        }
    }
}