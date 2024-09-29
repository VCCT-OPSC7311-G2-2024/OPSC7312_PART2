package com.example.opsc7312poe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var userMoodsRef: DatabaseReference
    private var user: FirebaseUser? = null
    private var selectedMood: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        val radIcon: ImageView = findViewById(R.id.rad)
        val goodIcon: ImageView = findViewById(R.id.good)
        val mehIcon: ImageView = findViewById(R.id.meh)
        val badIcon: ImageView = findViewById(R.id.bad)
        val awfulIcon: ImageView = findViewById(R.id.awful)
        val saveMoodButton: Button = findViewById(R.id.save_mood)

        user = auth.currentUser

        if (user == null) {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        } else {
            val userId = user!!.uid
            val database = FirebaseDatabase.getInstance("https://opsc7311poe-fd06a-default-rtdb.europe-west1.firebasedatabase.app")
            userMoodsRef = database.getReference("users").child(userId).child("moods")

            radIcon.setOnClickListener { onMoodSelected("rad") }
            goodIcon.setOnClickListener { onMoodSelected("good") }
            mehIcon.setOnClickListener { onMoodSelected("meh") }
            badIcon.setOnClickListener { onMoodSelected("bad") }
            awfulIcon.setOnClickListener { onMoodSelected("awful") }

            saveMoodButton.setOnClickListener {
                if (selectedMood != null) {
                    saveMood(selectedMood!!)
                } else {
                    Toast.makeText(this, "Please select a mood", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val bottomNavView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_audio -> {
                    val intent = Intent(this, AudioActivity::class.java)
                    startActivity(intent)
                    true
                }
            //    R.id.nav_journal -> {
            //        val intent = Intent(this, JournalActivity::class.java)
            //        startActivity(intent)
            //        true
            //    }
            //    R.id.nav_profile -> {
            //        val intent = Intent(this, ProfileActivity::class.java)
            //        startActivity(intent)
            //        true
            //    }
                else -> false
            }
        }
    }

    private fun onMoodSelected(mood: String) {
        selectedMood = mood
        Toast.makeText(this, "Mood selected: $mood", Toast.LENGTH_SHORT).show()
    }

    private fun saveMood(mood: String) {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        userMoodsRef.child(currentDate).child("mood").setValue(mood)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Mood saved!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MoodTrack::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Failed to save mood", Toast.LENGTH_SHORT).show()
                }
            }
    }
}

