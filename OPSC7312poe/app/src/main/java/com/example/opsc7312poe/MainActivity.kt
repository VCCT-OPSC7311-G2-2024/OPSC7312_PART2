package com.example.opsc7312poe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var userMoodsRef: DatabaseReference
    private var user: FirebaseUser? = null

    private var selectedMood: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase App
        FirebaseApp.initializeApp(this)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Initialize mood icons and other views
        val radIcon: ImageView = findViewById(R.id.rad)
        val goodIcon: ImageView = findViewById(R.id.good)
        val mehIcon: ImageView = findViewById(R.id.meh)
        val badIcon: ImageView = findViewById(R.id.bad)
        val awfulIcon: ImageView = findViewById(R.id.awful)
        val saveMoodButton: Button = findViewById(R.id.save_mood)

        // Navigation buttons
        val navProfile: ImageButton = findViewById(R.id.nav_profile)
        val navJournal: ImageButton = findViewById(R.id.nav_journal)

        // Get the current user
        user = auth.currentUser

        // Check if the user is null
        if (user == null) {
            // If user is not logged in, redirect to LoginActivity
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        } else {
            // Initialize Firebase Database reference for user's moods
            val userId = user!!.uid
            val database =
                FirebaseDatabase.getInstance("https://opsc7311poe-fd06a-default-rtdb.europe-west1.firebasedatabase.app")
            userMoodsRef = database.getReference("users").child(userId).child("moods")

            Log.d("MainActivity", "Firebase reference: $userMoodsRef")

            // Set click listeners on mood icons
            radIcon.setOnClickListener { onMoodSelected("rad") }
            goodIcon.setOnClickListener { onMoodSelected("good") }
            mehIcon.setOnClickListener { onMoodSelected("meh") }
            badIcon.setOnClickListener { onMoodSelected("bad") }
            awfulIcon.setOnClickListener { onMoodSelected("awful") }

            // Save mood when Save button is clicked
            saveMoodButton.setOnClickListener {
                Log.d("MainActivity", "Save Mood button clicked.")
                if (selectedMood != null) {
                    Log.d("MainActivity", "Selected mood: $selectedMood")
                    saveMood(selectedMood!!)

                    // Pass the selected mood to ComposeActivity
                    val intent = Intent(this, ComposeActivity::class.java).apply {
                        putExtra("selectedMood", selectedMood) // Pass the selected mood
                    }
                    startActivity(intent) // Navigate to ComposeActivity
                } else {
                    Toast.makeText(this, "Please select a mood", Toast.LENGTH_SHORT).show()
                }
            }

            // Set up navigation
            navProfile.setOnClickListener {
                val intent = Intent(this, AccountActivity::class.java)
                startActivity(intent)
            }

            navJournal.setOnClickListener {
                val intent = Intent(this, ComposeActivity::class.java)
                startActivity(intent)
            }
        }
    }

    // Method for handling mood selection
    private fun onMoodSelected(mood: String) {
        selectedMood = mood
        Toast.makeText(this, "Mood selected: $mood", Toast.LENGTH_SHORT).show()
    }

    // Method for saving the mood to Firebase
    private fun saveMood(mood: String) {
        // Get current date in 'yyyy-MM-dd' format
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        // Log the current date and the mood value
        Log.d("MainActivity", "Attempting to save mood: $mood for date: $currentDate")

        // Log the full Firebase path
        val firebasePath = "users/${user?.uid}/moods/$currentDate/mood"
        Log.d("MainActivity", "Firebase Path: $firebasePath")

        // Save the mood for the current date in Firebase
        userMoodsRef.child(currentDate).child("mood").setValue(mood)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Mood saved successfully
                    Log.d("MainActivity", "Successfully saved mood: $mood for date: $currentDate")
                    Toast.makeText(this, "Mood saved!", Toast.LENGTH_SHORT).show()

                    // Optionally, start the MoodTrack activity if needed
                    val intent = Intent(this, MoodTrack::class.java)
                    startActivity(intent)
                } else {
                    // Log the error in case of failure
                    val errorMessage = task.exception?.message
                    Log.e(
                        "MainActivity",
                        "Failed to save mood for date: $currentDate. Error: $errorMessage"
                    )
                    Toast.makeText(this, "Failed to save mood: $errorMessage", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }
}
