package com.example.audioapp

import android.os.Bundle
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio) // Make sure this layout file exists

        // Prepare audio titles and corresponding icons
        val audioFiles = listOf(
            Pair("Audio 1", R.raw.audio1),
            Pair("Audio 2", R.raw.audio2),
            Pair("Audio 3", R.raw.audio3)
        )

        val audioIcons = listOf(
            R.drawable.ic_audio1,
            R.drawable.ic_audio2,
            R.drawable.ic_audio3
        )

        // Set up the GridView and adapter
        val gridView: GridView = findViewById(R.id.audioGridView)
        val adapter = MyAudioAdapter(this, audioFiles, audioIcons)
        gridView.adapter = adapter
    }
}

