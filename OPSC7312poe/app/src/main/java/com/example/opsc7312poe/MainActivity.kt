package com.example.opsc7312poe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var button: Button
    private lateinit var textView: TextView
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance()

        // Bind views
        button = findViewById(R.id.logout)
        textView = findViewById(R.id.user_details) // Fixed ID name (no '.' in IDs)

        // Get the current user
        user = auth.currentUser!!

        // Check if the user is null
        if (user == null) {
            // If user is not logged in, redirect to LoginActivity
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        }
        else {
            textView.text = user.email // Use Kotlin property syntax for setting text
        }

        button.setOnClickListener {
            FirebaseAuth.getInstance().signOut() // Signing out the user
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        }

    }
}
