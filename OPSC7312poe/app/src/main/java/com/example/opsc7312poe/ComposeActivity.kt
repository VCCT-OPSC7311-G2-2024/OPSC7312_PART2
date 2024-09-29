package com.example.opsc7312poe

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.opsc7312poe.R.*
import com.example.opsc7312poe.R.id.backButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ComposeActivity : AppCompatActivity() {

    private lateinit var bodyEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var moodIcon: ImageView
    private lateinit var backButton: TextView
    private lateinit var databaseRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_compose)

        // Initialize Firebase components
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().getReference("journalEntries")

        // Get UI elements
        bodyEditText = findViewById(id.journalBody)
        saveButton = findViewById(id.saveEntryButton)
        moodIcon = findViewById(id.moodIcon)
        backButton = findViewById(id.backButton)

        // Set up the back button
        backButton.setOnClickListener {
            finish()
        }

        // Retrieve the selected mood from the intent
        val selectedMood = intent.getStringExtra("selectedMood")
        if (selectedMood != null) {
            setMoodIcon(selectedMood)
        }

        // Set up save button listener
        saveButton.setOnClickListener {
            saveEntry()
        }
    }

    private fun setMoodIcon(selectedMood: String) {
        when (selectedMood) {
            "rad" -> moodIcon.setImageResource(drawable.rad)
            "good" -> moodIcon.setImageResource(drawable.good)
            "meh" -> moodIcon.setImageResource(drawable.meh)
            "bad" -> moodIcon.setImageResource(drawable.bad)
            "awful" -> moodIcon.setImageResource(drawable.awful)
            else -> moodIcon.setImageResource(drawable.good)
        }
    }

    private fun saveEntry() {
        val body = bodyEditText.text.toString().trim()
        val userId = auth.currentUser?.uid ?: return

        if (body.isNotEmpty()) {
            val entryId = databaseRef.push().key ?: return
            val entry = JournalEntry(entryId, userId, "Untitled", body)
            databaseRef.child(userId).child(entryId).setValue(entry).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Entry saved successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to save entry", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }
}

data class JournalEntry(val entryId: String, val userId: String, val title: String, val body: String)
