package com.example.audioapp

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AudioActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer

    private val audioFiles = listOf(
        Pair("Audio 1", R.raw.audio1),
        Pair("Audio 2", R.raw.audio2),
        Pair("Audio 3", R.raw.audio3)
    )

    private val audioIcons = listOf(
        R.drawable.ic_audio1,
        R.drawable.ic_audio2,
        R.drawable.ic_audio3
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)

        val audioGridView: GridView = findViewById(R.id.audioGridView)

        val adapter = AudioAdapter(audioFiles, audioIcons)
        audioGridView.adapter = adapter

        audioGridView.setOnItemClickListener { _, _, position, _ ->
            playAudio(audioFiles[position].second)
        }
    }

    private fun playAudio(audioResId: Int) {
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
        mediaPlayer = MediaPlayer.create(this, audioResId)
        mediaPlayer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}
class AudioAdapter(
    private val audioFiles: List<Pair<String, Int>>,
    private val audioIcons: List<Int>
) : BaseAdapter() {

    override fun getCount(): Int = audioFiles.size

    override fun getItem(position: Int): Any = audioFiles[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(parent?.context)
            .inflate(R.layout.list_item_audio, parent, false)

        val audioIcon: ImageView = view.findViewById(R.id.audioIconImageView)
        val audioTitle: TextView = view.findViewById(R.id.audioTitleTextView)

        audioIcon.setImageResource(audioIcons[position])
        audioTitle.text = audioFiles[position].first

        return view
    }
}
