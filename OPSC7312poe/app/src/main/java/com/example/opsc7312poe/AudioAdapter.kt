package com.example.opsc7312poe

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AudioAdapter(private var audioList: MutableList<AudioItem>, private val context: Context) :
    RecyclerView.Adapter<AudioAdapter.AudioViewHolder>(), Filterable {

    private var audioListFull: MutableList<AudioItem> = ArrayList(audioList) // Copy of the full list for filtering
    private var mediaPlayer: MediaPlayer? = null
    private var currentlyPlayingPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.audio_item, parent, false)
        return AudioViewHolder(view)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        val audioItem = audioList[holder.adapterPosition]
        holder.title.text = audioItem.title
        holder.description.text = audioItem.description
        holder.icon.setImageResource(audioItem.iconResId)

        if (holder.adapterPosition == currentlyPlayingPosition) {
            holder.playPauseButton.setImageResource(R.drawable.ic_pause)
        } else {
            holder.playPauseButton.setImageResource(R.drawable.ic_play)
        }

        holder.playPauseButton.setOnClickListener {
            val currentPosition = holder.adapterPosition
            if (currentPosition == RecyclerView.NO_POSITION) return@setOnClickListener

            if (currentPosition == currentlyPlayingPosition) {
                mediaPlayer?.pause()
                currentlyPlayingPosition = -1
                notifyItemChanged(currentPosition)
            } else {
                mediaPlayer?.release()
                mediaPlayer = MediaPlayer.create(context, Uri.parse(audioItem.fileUrl))
                mediaPlayer?.start()
                notifyItemChanged(currentlyPlayingPosition)
                currentlyPlayingPosition = currentPosition
                notifyItemChanged(currentPosition)
            }
        }
    }

    override fun getItemCount() = audioList.size

    override fun getFilter(): Filter {
        return audioFilter
    }

    private val audioFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = mutableListOf<AudioItem>()

            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(audioListFull)
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim()

                for (item in audioListFull) {
                    if (item.title.toLowerCase().contains(filterPattern) ||
                        item.description.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }

            val results = FilterResults()
            results.values = filteredList
            return results
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            audioList.clear()
            audioList.addAll(results?.values as List<AudioItem>)
            notifyDataSetChanged()
        }
    }

    class AudioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.audioTitle)
        val description: TextView = itemView.findViewById(R.id.audioDescription)
        val icon: ImageView = itemView.findViewById(R.id.audioIcon)
        val playPauseButton: ImageView = itemView.findViewById(R.id.playPauseButton)
    }
}