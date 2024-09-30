package com.example.opsc7312poe

data class AudioItem (
    val title: String,
    val description: String,
    val fileUrl: String,
    val iconResId: Int,
    var isPlaying: Boolean = false
)