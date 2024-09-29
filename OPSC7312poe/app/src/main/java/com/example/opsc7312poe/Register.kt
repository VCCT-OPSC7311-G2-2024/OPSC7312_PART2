package com.example.opsc7312poe

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class Register : AppCompatActivity() {

    private lateinit var nameEditText: TextInputEditText
    private lateinit var surnameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var confirmPasswordEditText: TextInputEditText
    private lateinit var registerButton: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var loginTextView: TextView

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register) // Ensure this matches your layout file

        // Initialize the views with matching IDs from the layout
        nameEditText = findViewById(R.id.editTextName)
        surnameEditText = findViewById(R.id.editTextSurname)
        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword)
        progressBar = findViewById(R.id.progressBar)
        mAuth = FirebaseAuth.getInstance()
        registerButton = findViewById(R.id.buttonRegister)
        loginTextView = findViewById(R.id.textViewLogin)

        // Set login text click listener
        loginTextView.setOnClickListener {
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        }

        // Set window insets listener
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set button click listener
        registerButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()
            val name = nameEditText.text.toString()
            val surname = surnameEditText.text.toString()

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(surname)) {
                Toast.makeText(this, "Enter your surname", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(this, "Confirm your password", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }

            // Registration logic
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(baseContext, "Account Created.", Toast.LENGTH_SHORT).show()
                        // Optionally navigate to the main activity or login screen
                    } else {
                        // If sign in fails, display a message to the user
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
