package com.example.dressme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val TAG: String = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener{
            performLogin();
        }
    }


    private fun performLogin() {
        if (!valid()) return

        val email       = email_edittext_login.text.toString()
        val password    = password_edittext_login.text.toString()

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (!it.isSuccessful)   return@addOnCompleteListener

                // else if successful
                Log.d(TAG, "Successfully logged in ${it.result?.user?.uid}")
                val intent = Intent(this, ProfileMainActivity::class.java)
                startActivity(intent);
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to login ${it.message}")
            }
    }


    private fun valid(): Boolean {
        val email       = email_edittext_login.text.toString()
        val password    = password_edittext_login.text.toString()

        Log.d(TAG, "Email is  $email")
        Log.d(TAG, "Password is  $password")

        if(email.isEmpty() || password.isEmpty())
            return false

        return true
    }
}
