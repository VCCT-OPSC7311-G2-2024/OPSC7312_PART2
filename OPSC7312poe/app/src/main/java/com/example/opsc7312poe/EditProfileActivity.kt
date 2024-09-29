package com.example.opsc7312poe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class EditProfileActivity : AppCompatActivity() {

    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var saveProfileButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        firstNameEditText = findViewById(R.id.editFirstName)
        lastNameEditText = findViewById(R.id.editLastName)
        saveProfileButton = findViewById(R.id.saveProfileButton)

        // Get the current name passed from AccountActivity
        val currentName = intent.getStringExtra("currentName") ?: ""
        val nameParts = currentName.split(" ")
        if (nameParts.size > 1) {
            firstNameEditText.setText(nameParts[0])
            lastNameEditText.setText(nameParts[1])
        }

        // Save the profile and return the updated name
        saveProfileButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString().trim()
            val lastName = lastNameEditText.text.toString().trim()
            val updatedName = "$firstName $lastName"

            // Return the updated name to AccountActivity
            val resultIntent = Intent()
            resultIntent.putExtra("updatedName", updatedName)
            setResult(RESULT_OK, resultIntent)
            finish() // Close the EditProfileActivity
        }
    }
}
