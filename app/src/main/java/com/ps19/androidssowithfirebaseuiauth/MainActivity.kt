package com.ps19.androidssowithfirebaseuiauth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 123 // Request code for FirebaseUI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create layout and buttons programmatically
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = android.view.Gravity.CENTER
        }

        val signInButton = Button(this).apply {
            text = "Sign In"
        }

        val logOutButton = Button(this).apply {
            text = "Log Out"
        }
        // Add buttons to the layout
        layout.addView(signInButton)
        layout.addView(logOutButton)

        setContentView(layout)

        // Set click listeners
        signInButton.setOnClickListener { startSignInFlow() }
        logOutButton.setOnClickListener { logOut() }
    }

    private fun startSignInFlow() {
        // Configure the list of sign-in providers (Google and Microsoft in this case)
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),    // Google Sign-In
            AuthUI.IdpConfig.MicrosoftBuilder().build()  // Microsoft Sign-In
        )

        // Create the sign-in intent using Firebase UI Auth
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setIsSmartLockEnabled(false)
            .setAvailableProviders(providers) // Available sign-in providers
           // .setLogo(R.drawable.firebase_logo) // Optional: Add a custom logo (replace with your logo)
         //   .setTheme(R.style.FirebaseUITheme) // Optional: Add a custom theme
            .build()

        // Start the sign-in activity with the request code
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun logOut() {
        // Sign out the user from Firebase
        FirebaseAuth.getInstance().signOut()
        clearAccountCache()
        Log.d("FirebaseAuth", "User logged out.")
    }

    private fun clearAccountCache() {
        // Clear account cache using AuthUI
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                Log.d("FirebaseAuth", "Account cache cleared. You can now sign in with a different account.")
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Check if the result is from the sign-in flow
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                Log.d("FirebaseAuth", "User: ${user?.displayName}, Email: ${user?.email}")
            } else {
                // Sign-in failed
                Log.e("FirebaseAuth", "Error: ${response?.error?.errorCode}")
            }
        }
    }
}
