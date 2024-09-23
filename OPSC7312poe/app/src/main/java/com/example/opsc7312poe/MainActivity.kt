package com.example.opsc7312poe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private var selectedMood: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize mood icons
        val radIcon: ImageView = findViewById(R.id.rad)
        val goodIcon: ImageView = findViewById(R.id.good)
        val mehIcon: ImageView = findViewById(R.id.meh)
        val badIcon: ImageView = findViewById(R.id.bad)
        val awfulIcon: ImageView = findViewById(R.id.awful)
        val saveMoodButton: Button = findViewById(R.id.save_mood)

        // Set click listeners on mood icons
        radIcon.setOnClickListener { onMoodSelected("rad") }
        goodIcon.setOnClickListener { onMoodSelected("good") }
        mehIcon.setOnClickListener { onMoodSelected("meh") }
        badIcon.setOnClickListener { onMoodSelected("bad") }
        awfulIcon.setOnClickListener { onMoodSelected("awful") }

        // Save mood when Save button is clicked
        saveMoodButton.setOnClickListener {
            if (selectedMood != null) {
                saveMood(selectedMood!!)
                Toast.makeText(this, "Mood saved: $selectedMood", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please select a mood", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onMoodSelected(mood: String) {
        selectedMood = mood
        Toast.makeText(this, "Mood selected: $mood", Toast.LENGTH_SHORT).show()
    }

    private fun saveMood(mood: String) {
        // Save the mood, e.g., to SharedPreferences
        val sharedPreferences = getSharedPreferences("MoodPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("selected_mood", mood)
        editor.apply()
    }
}
