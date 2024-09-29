package com.example.opsc7312poe

import android.os.Bundle
import android.media.MediaPlayer
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AudioActivity : AppCompatActivity() {
    private lateinit var audioRecyclerView: RecyclerView
    private lateinit var audioAdapter: AudioAdapter
    private var audioList: MutableList<AudioItem> = mutableListOf()
    private lateinit var searchBar: EditText
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)

        // Initialize RecyclerView and search bar
        audioRecyclerView = findViewById(R.id.audioRecyclerView)
        searchBar = findViewById(R.id.searchBar)

        // Initialize the audio list with 15 audio items
        audioList.apply {
            add(AudioItem("Relaxing Music", "Calm and soothing", "https://drive.google.com/uc?export=download&id=1GG_pxHoCe2GWRWr-jQc469lGpqbs8rKL", R.drawable.ic_audio4))
            add(AudioItem("Meditation Guide", "Guided meditation", "https://drive.google.com/uc?export=download&id=1aoQPJINbtdi2KJBJcUHamcZdmz4Mf_Fi", R.drawable.ic_audio5))
            add(AudioItem("Anxiety Relief", "An audio to help combat anxiety", "https://drive.google.com/uc?export=download&id=12IB5tp3Mf4lA_oCB2P9y9KuWLhYIZAkZ", R.drawable.ic_audio2))
            add(AudioItem("Ocean Waves", "Sounds of the ocean", "https://drive.google.com/uc?export=download&id=1wVJo1zQik2yZ-K6AeWdaxmircmePLtr7", R.drawable.ic_audio6))
            add(AudioItem("Rain Sounds", "Soothing rain sounds", "https://drive.google.com/uc?export=download&id=18zfKDvAN8J6l24pYwWFoIdUAmX55RfZ_", R.drawable.ic_audio7))
            add(AudioItem("Bedtime story 1", "A bedtime story", "https://drive.google.com/uc?export=download&id=13kXmchJy1PWyExwdQ8BLoiM4kaThSf5i", R.drawable.ic_audio3))
            add(AudioItem("Bedtime story 2", "A bedtime story", "https://drive.google.com/uc?export=download&id=1y9wJ3yy7WDq11BJj0Nxpw5UVs-rB09h3", R.drawable.ic_audio8))
            add(AudioItem("Bedtime story 3", "A bedtime story", "https://drive.google.com/uc?export=download&id=16dT9fPNQPAzNP5XSj6gRmHQph9Y14NwO", R.drawable.ic_audio9))
            add(AudioItem("Motivational Speech", "Uplifting speech", "https://drive.google.com/uc?export=download&id=1ewNF46vUlvgoyvWbamiubTZlZZpstVtV", R.drawable.ic_audio1))
            add(AudioItem("Deep Breathing", "Guided deep breathing exercise", "https://drive.google.com/uc?export=download&id=1Oo_KqSR-r0eZw-K2EwAAc4yAjQkl9ovd", R.drawable.ic_audio10))
            add(AudioItem("Yoga Music", "Music for yoga practice", "https://drive.google.com/uc?export=download&id=1VHFGRDcmBzK6Ac3qIT_cuboi6pClD4-z", R.drawable.ic_audio11))
            add(AudioItem("Mindfulness Meditation", "Mindfulness meditation session", "https://drive.google.com/uc?export=download&id=14Bf0i-U-XPLXTjgdq83nCOEnLa24l-KR", R.drawable.ic_audio12))
            add(AudioItem("Instrumental Music", "Relaxing instrumental music", "https://drive.google.com/uc?export=download&id=1MwxU_J3MOwwdOsE6jxb6W43uns8dbJIn", R.drawable.ic_audio13))
            add(AudioItem("Affirmations", "Positive affirmations", "https://drive.google.com/uc?export=download&id=1TSWw8lXYMQj6cgglYp2Pdpnld4XQ9Dq5", R.drawable.ic_audio14))
            add(AudioItem("Healing Sounds", "Sounds for healing", "https://drive.google.com/uc?export=download&id=1zvhnmuiMVT43RNVbbm4o9-nF98_WnvWD", R.drawable.ic_audio15))
        }

        // Initialize the adapter
        audioAdapter = AudioAdapter(audioList, this)
        audioRecyclerView.layoutManager = LinearLayoutManager(this)
        audioRecyclerView.adapter = audioAdapter

        // Add text change listener to the search bar
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                audioAdapter.getFilter().filter(s.toString())
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }
    // Function to play audio
    fun playAudio(audioUrl: String) {
        // Release any existing MediaPlayer
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer()

        try {
            mediaPlayer?.setDataSource(audioUrl)
            mediaPlayer?.prepare() // Prepare the MediaPlayer
            mediaPlayer?.start() // Start playback
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release() // Release resources when activity is destroyed
        mediaPlayer = null
    }
}

