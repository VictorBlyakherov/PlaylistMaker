package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.search)
        val buttonMedia = findViewById<Button>(R.id.media)
        val buttonSettings = findViewById<Button>(R.id.settings)

        buttonSettings.setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }

        buttonSearch.setOnClickListener {
            val displayIntent = Intent(this, SearchActivity::class.java)
            startActivity(displayIntent)
        }

        buttonMedia.setOnClickListener {
            val displayIntent = Intent(this, MediaActivity::class.java)
            startActivity(displayIntent)
        }

    }
}