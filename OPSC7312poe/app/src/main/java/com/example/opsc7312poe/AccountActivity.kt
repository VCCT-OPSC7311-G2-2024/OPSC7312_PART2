package com.example.opsc7312poe

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class AccountActivity : AppCompatActivity() {

    private lateinit var userNameTextView: TextView
    private lateinit var editProfileButton: Button
    private lateinit var securityButton: Button
    private lateinit var notificationsButton: Button
    private lateinit var privacyButton: Button
    private lateinit var helpSupportButton: Button
    private lateinit var termsPoliciesButton: Button
    private lateinit var logoutButton: TextView
    private lateinit var settingsButton: TextView
    private lateinit var auth: FirebaseAuth

    // Request code to identify the result from EditProfileActivity
    private val EDIT_PROFILE_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        auth = FirebaseAuth.getInstance()
        userNameTextView = findViewById(R.id.userName)
        editProfileButton = findViewById(R.id.editProfileButton)
        securityButton = findViewById(R.id.securityButton)
        notificationsButton = findViewById(R.id.notificationsButton)
        privacyButton = findViewById(R.id.privacyButton)
        helpSupportButton = findViewById(R.id.helpSupportButton)
        termsPoliciesButton = findViewById(R.id.termsPoliciesButton)
        logoutButton = findViewById(R.id.logoutButton)
        settingsButton = findViewById(R.id.settingsButton)

        // Set username or email
        val user = auth.currentUser
        userNameTextView.text = user?.displayName ?: "Victoria Robertson"

        // Edit profile button click listener
        editProfileButton.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            // Pass the current username to EditProfileActivity
            intent.putExtra("currentName", userNameTextView.text.toString())
            startActivityForResult(intent, EDIT_PROFILE_REQUEST_CODE)
        }

        securityButton.setOnClickListener {
            Toast.makeText(this, "Security Settings", Toast.LENGTH_SHORT).show()
        }

        notificationsButton.setOnClickListener {
            Toast.makeText(this, "Notification Settings", Toast.LENGTH_SHORT).show()
        }

        privacyButton.setOnClickListener {
            Toast.makeText(this, "Privacy Settings", Toast.LENGTH_SHORT).show()
        }

        helpSupportButton.setOnClickListener {
            Toast.makeText(this, "Help & Support", Toast.LENGTH_SHORT).show()
        }

        termsPoliciesButton.setOnClickListener {
            Toast.makeText(this, "Terms and Policies", Toast.LENGTH_SHORT).show()
        }

        logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, Login::class.java))
            finish()
        }

        settingsButton.setOnClickListener {
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
        }
    }

    // Handle the result from EditProfileActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == EDIT_PROFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Get the updated name from the result
            val updatedName = data?.getStringExtra("updatedName")
            if (updatedName != null) {
                userNameTextView.text = updatedName
            }
        }
    }
}
