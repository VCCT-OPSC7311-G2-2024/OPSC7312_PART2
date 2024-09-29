package com.example.audioapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class MyAudioAdapter(
    private val context: Context,
    private val audioFiles: List<Pair<String, Int>>,
    private val audioIcons: List<Int>
) : BaseAdapter() {

    override fun getCount(): Int {
        return audioFiles.size
    }

    override fun getItem(position: Int): Any {
        return audioFiles[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_audio, parent, false)

        val audioIcon: ImageView = view.findViewById(R.id.audioIconImageView) // Ensure this matches your XML
        val audioTitle: TextView = view.findViewById(R.id.audioTitleTextView) // Ensure this matches your XML

        // Set the title and icon
        audioTitle.text = audioFiles[position].first // The title from the Pair
        audioIcon.setImageResource(audioIcons[position]) // The corresponding icon

        return view
    }
}
